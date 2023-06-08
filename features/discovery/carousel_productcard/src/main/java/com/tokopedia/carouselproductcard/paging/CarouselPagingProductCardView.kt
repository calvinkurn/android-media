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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlin.math.max


class CarouselPagingProductCardView: ConstraintLayout {

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

    private var binding: CarouselPagingProductCardLayoutBinding? = null
    private val config = AttributesConfig()
    private val adapter = Adapter(TypeFactoryImpl())
    private val layoutManager =
        GridLayoutManager(context, config.itemPerPage, HORIZONTAL, false).also {
            it.spanSizeLookup = spanSizeLookup()
        }
    private val snapHelper = StartPagerSnapHelper()

    private fun spanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val visitable = adapter.getItemAt(position)
            return if (visitable is Spannable) visitable.spanSize else 1
        }
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

            addDivider()
            addPaginationSnap()
        }
    }

    private fun RecyclerView.addDivider() {
        if (itemDecorationCount > 0) return

        addItemDecoration(CarouselPagingItemDecoration(context))
    }

    private fun RecyclerView.addPaginationSnap() {
        snapHelper.attachToRecyclerView(this)
    }

    private fun initPageControl() {
        binding?.pageControlCarouselPaging?.showWithCondition(config.showPagingIndicator)
    }

    fun setPagingModel(
        model: CarouselPagingModel,
        listener: CarouselPagingListener,
        recycledViewPool: RecycledViewPool = RecycledViewPool(),
    ) {
        binding?.carouselPagingProductCardRecyclerView?.run {
            setRecycledViewPool(recycledViewPool)
            addOnScrollListener(groupPaginationOnScroll(model, listener))
        }

        val visitableList = VisitableFactory.from(model, config.itemPerPage)
        adapter.submitList(visitableList)

        configurePageControl(model)

        scrollToCurrentPage(visitableList, model)
    }

    private fun scrollToCurrentPage(
        visitableList: List<Visitable<TypeFactory>>,
        model: CarouselPagingModel
    ) {
        val recyclerView = binding?.carouselPagingProductCardRecyclerView ?: return
        val position = visitableList.indexOfItemInCurrentPage(model)

        recyclerView.scrollToPosition(position)
        recyclerView.post {
            val view = layoutManager.findViewByPosition(position) ?: return@post
            val distance = snapHelper.calculateDistanceToFinalSnap(layoutManager, view)
            recyclerView.scrollBy(distance[0], distance[1])
        }
    }

    private fun List<Visitable<TypeFactory>>.indexOfItemInCurrentPage(model: CarouselPagingModel): Int {
        val itemIndex =
            if (model.currentPageInGroup == CarouselPagingModel.LAST_PAGE_IN_GROUP)
                indexOfLast { it.isInGroup(model.selectedGroup) }
            else
                indexOfFirst { it.isInPage(model.selectedGroup, model.currentPageInGroup) }

        return max(itemIndex, 0)
    }

    private fun Visitable<TypeFactory>.isInGroup(group: CarouselPagingGroupModel?) =
        this is HasGroup && this.group == group

    private fun Visitable<TypeFactory>.isInPage(group: CarouselPagingGroupModel?, pageInGroup: Int) =
        this is HasGroup
            && this.group == group
            && this.pageInGroup == pageInGroup

    private fun groupPaginationOnScroll(
        model: CarouselPagingModel,
        listener: CarouselPagingListener,
    ) = GroupPaginationOnScrollListener(
            currentGroup = model.selectedGroup,
            currentPageInGroup = model.currentPageInGroup,
            currentPageCount = model.selectedProductGroup?.getPageCount(config.itemPerPage) ?: 0,
            paginationListener = paginationListener(listener)
        )

    private fun paginationListener(listener: CarouselPagingListener) = object : PaginationListener {
        override fun onGroupChanged(group: SelectedGroupModel?) {
            group ?: return
            listener.onGroupChanged(group)
        }

        override fun onPageCountChanged(pageCount: Int) {
            binding?.pageControlCarouselPaging?.setIndicator(max(pageCount, 1))
        }

        override fun onPageInGroupChanged(pageInGroup: Int) {
            binding?.pageControlCarouselPaging?.setCurrentIndicator(pageInGroup - 1)
        }
    }

    private fun configurePageControl(model: CarouselPagingModel) {
        val selectedProductGroup = model.selectedProductGroup ?: return

        binding?.pageControlCarouselPaging?.run {
            setIndicator(selectedProductGroup.getPageCount(config.itemPerPage))
            setCurrentIndicator(model.currentPageInGroup - 1)
        }
    }

    fun recycle() {
        adapter.submitList(null)
    }

    interface CarouselPagingListener {
        fun onGroupChanged(selectedGroupModel: SelectedGroupModel)
    }
}
