package com.tokopedia.carouselproductcard.paging

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.databinding.CarouselPagingProductCardLayoutBinding
import com.tokopedia.carouselproductcard.helper.StartPagerSnapHelper
import com.tokopedia.carouselproductcard.paging.GroupPaginationOnScrollListener.PaginationListener
import com.tokopedia.device.info.DevicePerformanceInfo
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlin.math.max


class CarouselPagingProductCardView: ConstraintLayout {

    private var binding: CarouselPagingProductCardLayoutBinding? = null
    private val config = AttributesConfig()
    private val adapter: Adapter by lazy {
        Adapter(TypeFactoryImpl(config.pagingPaddingHorizontal))
    }
    private val layoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, config.itemPerPage, HORIZONTAL, false).apply {
            spanSizeLookup = spanSizeLookup()
        }
    }
    private val snapHelper: StartPagerSnapHelper by lazy {
        StartPagerSnapHelper(config.pagingPaddingHorizontal, config.itemPerPage)
    }
    private var groupPaginationOnScrollListener: GroupPaginationOnScrollListener? = null
    private val showPagingIndicator
        get() = config.showPagingIndicator

    private fun spanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val visitable = adapter.getItemAt(position) ?: return 1
            return if (visitable is Spannable) visitable.spanSize else 1
        }
    }

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        initAttributes(attrs)
        initBinding()
        initRecyclerView()
        initPageControl()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        config.load(context, attrs)
    }

    private fun initBinding() {
        binding = CarouselPagingProductCardLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
        )
    }

    private fun initRecyclerView() {
        binding?.carouselPagingProductCardRecyclerView?.run {
            adapter = this@CarouselPagingProductCardView.adapter
            layoutManager = this@CarouselPagingProductCardView.layoutManager
            itemAnimator = null

            addDivider()
            addPaginationSnap()

            setHasFixedSize(true)
        }
    }

    private fun RecyclerView.addDivider() {
        if (itemDecorationCount > 0) return

        val itemDecoration = ItemDecoration(
            context,
            config.pagingPaddingHorizontal,
            config.itemPerPage,
        )
        itemDecoration.setDrawable(ItemDecoration.createDrawable(context))
        addItemDecoration(itemDecoration)
    }

    private fun RecyclerView.addPaginationSnap() {
        snapHelper.attachToRecyclerView(this)
    }

    private fun initPageControl() {
        binding?.pageControlCarouselPaging?.showWithCondition(showPagingIndicator)
    }

    fun setPagingModel(
        model: CarouselPagingModel,
        listener: CarouselPagingListener,
        recycledViewPool: RecycledViewPool? = RecycledViewPool(),
    ) {
        binding?.carouselPagingProductCardRecyclerView?.run {
            setRecycledViewPool(recycledViewPool)
            setupOnScrollListener(model, listener)
        }

        val visitableList = VisitableFactory.from(model, config.itemPerPage, listener)
        adapter.submitList(visitableList)

        configurePageControl(model)

        scrollToCurrentPage(visitableList, model)
    }

    private fun RecyclerView.setupOnScrollListener(
        model: CarouselPagingModel,
        listener: CarouselPagingListener
    ) {
        groupPaginationOnScrollListener?.let {
            removeOnScrollListener(it)
        }

        groupPaginationOnScrollListener = groupPaginationOnScroll(model, listener).also {
            addOnScrollListener(it)
        }
    }

    private fun scrollToCurrentPage(
        visitableList: List<Visitable<TypeFactory>>,
        model: CarouselPagingModel,
    ) {
        val position =
            visitableList.indexOfItemInGroup(model.selectedGroup, model.currentPageInGroup)

        binding?.carouselPagingProductCardRecyclerView?.scrollToPage(position)
    }

    private fun List<Visitable<TypeFactory>>.indexOfItemInGroup(
        group: CarouselPagingGroupModel?,
        pageInGroup: Int,
    ): Int {
        val itemIndex =
            if (pageInGroup == CarouselPagingModel.LAST_PAGE_IN_GROUP)
                indexOfLast { it.isInGroup(group) }
            else
                indexOfFirst { it.isInPage(group, pageInGroup) }

        return max(itemIndex, 0)
    }

    private fun Visitable<TypeFactory>.isInGroup(group: CarouselPagingGroupModel?) =
        this is HasGroup && this.group == group

    private fun Visitable<TypeFactory>.isInPage(group: CarouselPagingGroupModel?, pageInGroup: Int) =
        this is HasGroup
            && this.group == group
            && this.pageInGroup == pageInGroup

    private fun RecyclerView.scrollToPage(position: Int) {
        post {
            scrollToPosition(position)
            scrollByDistance(position)
        }
    }

    private fun RecyclerView.scrollByDistance(position: Int) {
        post {
            val layoutManager = this@CarouselPagingProductCardView.layoutManager
            val view = layoutManager.findViewByPosition(position) ?: return@post
            val distance = snapHelper.calculateDistanceToFinalSnap(layoutManager, view)
            scrollBy(distance[0], distance[1])
        }
    }

    private fun groupPaginationOnScroll(
        model: CarouselPagingModel,
        listener: CarouselPagingListener?,
    ) = GroupPaginationOnScrollListener(
            currentGroup = model.selectedGroup,
            currentPageInGroup = model.currentPageInGroup,
            currentPageCount = model.selectedProductGroup?.getPageCount(config.itemPerPage) ?: 0,
            paginationListener = paginationListener(listener)
        )

    private fun paginationListener(listener: CarouselPagingListener?) = object : PaginationListener {
        override fun onGroupChanged(group: CarouselPagingSelectedGroupModel?) {
            group ?: return
            listener?.onGroupChanged(group)
        }

        override fun onPageCountChanged(pageCount: Int) {
            if (!showPagingIndicator) return
            binding?.pageControlCarouselPaging?.setIndicator(max(pageCount, 1))
        }

        override fun onPageInGroupChanged(pageInGroup: Int) {
            if (!showPagingIndicator) return
            binding?.pageControlCarouselPaging?.setCurrentIndicator(pageInGroup - 1)
        }
    }

    private fun configurePageControl(model: CarouselPagingModel) {
        if (!showPagingIndicator) return

        val selectedProductGroup = model.selectedProductGroup ?: return

        binding?.pageControlCarouselPaging?.run {
            setIndicator(selectedProductGroup.getPageCount(config.itemPerPage))
            setCurrentIndicator(model.currentPageInGroup - 1)
        }
    }

    fun recycle() { }

    fun scrollToGroup(carouselPagingGroupModel: CarouselPagingGroupModel, pageInGroup: Int) {
        val visitableList = adapter.currentList
        val position = visitableList.indexOfItemInGroup(carouselPagingGroupModel, pageInGroup)

        binding?.carouselPagingProductCardRecyclerView?.scrollToPage(position)
    }

    interface CarouselPagingListener {
        fun onGroupChanged(selectedGroupModel: CarouselPagingSelectedGroupModel)
        fun onItemImpress(groupModel: CarouselPagingGroupModel, itemPosition: Int)
        fun onItemClick(groupModel: CarouselPagingGroupModel, itemPosition: Int)
    }
}
