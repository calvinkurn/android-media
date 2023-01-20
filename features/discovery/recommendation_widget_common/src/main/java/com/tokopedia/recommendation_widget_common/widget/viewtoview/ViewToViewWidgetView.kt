package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.content.Context
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.extension.toViewToViewItemModels
import com.tokopedia.recommendation_widget_common.viewutil.toDpInt
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView.RecomWidgetMetadata
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ViewToViewWidgetView : FrameLayout, ViewToViewItemListener, CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var itemView: View
    private val itemContext: Context
        get() = itemView.context
    private var basicListener: ViewToViewWidgetBasicListener? = null
    private var carouselData: RecommendationCarouselData? = null
    private var headerView: Typography? = null
    private var loadingView: View? = null
    private var typeFactory: ViewToViewItemTypeFactory? = null
    private var recyclerView: RecyclerView
    private var adapter: ViewToViewItemAdapter? = null
    private var localLoad: LocalLoad? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var scrollListener: ((Parcelable?) -> Unit)? = null
    private var userSession: UserSessionInterface? = null

    private var widgetMetadata: RecomWidgetMetadata = RecomWidgetMetadata()
    private var pageName: String = ""

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_widget_view_to_view, this)
        recyclerView = view.findViewById(R.id.rv_product)
        headerView = view.findViewById(R.id.tg_header)
        loadingView = view.findViewById(R.id.loading_view_to_view)
        localLoad = view.findViewById(R.id.ll_view_to_view)
        this.itemView = view
        this.userSession = UserSession(itemContext)
        initListeners()
    }

    private fun initListeners() {
        localLoad?.setOnClickListener {
            basicListener?.onViewToViewReload(pageName)
        }
    }

    /*
    * bind recom widget with data called from fragment
    *
     */
    fun bind(
        carouselData: RecommendationCarouselData = RecommendationCarouselData(),
        adapterPosition: Int = 0,
        basicListener: ViewToViewWidgetBasicListener?,
        scrollToPosition: Int = RecyclerView.NO_POSITION,
    ) {
        try {
            widgetMetadata = widgetMetadata.copy(
                adapterPosition = adapterPosition,
                scrollToPosition = scrollToPosition
            )
            this.basicListener = basicListener
            this.pageName = carouselData.recommendationData.pageName
            if (carouselData.recommendationData.recommendationItemList.isNotEmpty()) {
                bindWidgetWithData(carouselData)
            } else this.basicListener?.onWidgetFail(
                carouselData.recommendationData.pageName,
                MessageErrorException("")
            )
        } catch (e: Exception) {
            this.basicListener?.onWidgetFail(carouselData.recommendationData.pageName, e)
        }
    }

    fun showLoading() {
        headerView?.gone()
        recyclerView.gone()
        loadingView?.visible()
        localLoad?.gone()
    }

    fun setPageName(pageName: String) {
        this.pageName = pageName
    }

    fun showFailedState(tempHeaderName: String) {
        headerView?.let {
            it.text = tempHeaderName
            it.visible()
        }
        recyclerView.gone()
        loadingView?.gone()
        localLoad?.visible()
    }

    fun setBasicListener(listener: ViewToViewWidgetBasicListener) {
        this.basicListener = listener
    }

    fun getCurrentPosition(): Int {
        return layoutManager?.let {
            when (it) {
                is GridLayoutManager -> it.findFirstCompletelyVisibleItemPosition()
                is LinearLayoutManager -> it.findFirstCompletelyVisibleItemPosition()
                else -> 0
            }
        } ?: 0
    }

    fun restoreScrollState(scrollState: Parcelable?) {
        val layoutManager = layoutManager ?: return

        itemView.post {
            layoutManager.onRestoreInstanceState(scrollState)
        }
    }

    fun setScrollListener(scrollListener: ((Parcelable?) -> Unit)?) {
        this.scrollListener = scrollListener
    }

    override fun onViewToViewItemClicked(item: ViewToViewItemData, position: Int) {
        carouselData?.let {
            basicListener?.onViewToViewItemClicked(
                data = item,
                itemPosition = position,
                adapterPosition = widgetMetadata.adapterPosition,
            )
        }
    }

    private fun initVar() {
        if (widgetMetadata.isInitialized) return
        carouselData?.let {
            typeFactory = ViewToViewItemTypeFactoryImpl(this)
        }
        typeFactory?.let {
            adapter = ViewToViewItemAdapter(it)
        }

        recyclerView.addOnScrollListener(createScrollListener())
        recyclerView.addItemDecoration(ViewToViewItemDecoration())
        recyclerView.adapter = adapter

        widgetMetadata.isInitialized = true
    }

    private fun createLayoutManager(itemSize: Int): RecyclerView.LayoutManager {
        return if (itemSize > 2) createLinearLayoutManager() else createGridLayoutManager()
    }

    private fun createLinearLayoutManager(): LinearLayoutManager {
        return object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                    parent,
                    child,
                    rect,
                    immediate,
                    focusedChildVisible
                )
            }
        }
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        return object : GridLayoutManager(itemView.context, 2) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                    parent,
                    child,
                    rect,
                    immediate,
                    focusedChildVisible
                )
            }
        }
    }

    private fun impressChannel(carouselData: RecommendationCarouselData) {
        itemView.addOnImpressionListener(carouselData) {
            basicListener?.onViewToViewBannerImpressed(
                data = carouselData.recommendationData,
                adapterPosition = widgetMetadata.adapterPosition,
            )
        }
    }

    private fun updateLayoutManager(itemSize: Int) {
        layoutManager = createLayoutManager(itemSize)
        recyclerView.layoutManager = layoutManager
    }

    private fun setData(carouselData: RecommendationCarouselData) {
        val cardList = mutableListOf<Visitable<*>>()
        val productDataList =
            carouselData.recommendationData.recommendationItemList.toViewToViewItemModels()
        cardList.addAll(productDataList)
        if (cardList.size != 0) {
            typeFactory?.useBigLayout(cardList.size < 3)
            updateLayoutManager(cardList.size)
            adapter?.submitList(cardList)

            launch {
                try {
                    recyclerView.visible()
                    loadingView?.gone()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    private fun setHeaderComponent(carouselData: RecommendationCarouselData) {
        headerView?.text = carouselData.recommendationData.title
    }

    private fun scrollCarousel(scrollToPosition: Int) {
        val layoutManager = layoutManager ?: return
        if (scrollToPosition == RecyclerView.NO_POSITION) return

        itemView.post {
            val currentLayoutManager = layoutManager
            if (currentLayoutManager is LinearLayoutManager) {
                currentLayoutManager.scrollToPositionWithOffset(
                    scrollToPosition,
                    16f.toDpInt()
                )
            }
        }
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = layoutManager ?: return
                val scrollState = layoutManager.onSaveInstanceState()
                scrollListener?.invoke(scrollState)
            }
        }
    }

    private fun bindWidgetWithData(carouselData: RecommendationCarouselData) {
        this.carouselData = carouselData
        initVar()
        doActionBasedOnRecomState(carouselData.state,
            onLoad = {
                recyclerView.gone()
                loadingView?.visible()
                localLoad?.gone()
            },
            onReady = {
                headerView?.visible()
                loadingView?.gone()
                impressChannel(carouselData)
                setHeaderComponent(carouselData)
                setData(carouselData)
                recyclerView.show()
                scrollCarousel(widgetMetadata.scrollToPosition)
            },
            onFailed = {
                recyclerView.gone()
                loadingView?.gone()
                localLoad?.visible()
            }
        )
    }

    private fun doActionBasedOnRecomState(
        state: Int,
        onLoad: () -> Unit?,
        onReady: () -> Unit?,
        onFailed: () -> Unit?
    ) {
        when (state) {
            STATE_LOADING -> {
                onLoad.invoke()
            }
            STATE_READY -> {
                onReady.invoke()
            }
            STATE_FAILED -> {
                onFailed.invoke()
            }
        }
    }
}
