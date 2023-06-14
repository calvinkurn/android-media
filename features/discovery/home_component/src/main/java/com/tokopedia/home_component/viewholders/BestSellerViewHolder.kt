package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.carouselproductcard.paging.CarouselPagingSelectedGroupModel
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentBestSellerBinding
import com.tokopedia.home_component.listener.BestSellerChipListener
import com.tokopedia.home_component.listener.BestSellerListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.BestSellerChipAdapter
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class BestSellerViewHolder(
    itemView: View,
    private val bestSellerListener: BestSellerListener,
    private val recycledViewPool: RecycledViewPool? = null,
): AbstractViewHolder<BestSellerDataModel>(itemView),
    BestSellerChipListener {

    private var binding: HomeComponentBestSellerBinding? by viewBinding()
    private var bestSellerDataModel: BestSellerDataModel? = null
    private val chipsLayoutManager = LinearLayoutManager(
        itemView.context,
        RecyclerView.HORIZONTAL,
        false,
    )
    private val chipsAdapter = BestSellerChipAdapter(this)

    init {
        binding?.homeComponentBestSellerChipsRecyclerView?.run {
            adapter = chipsAdapter
            layoutManager = chipsLayoutManager
        }
    }

    override fun bind(element: BestSellerDataModel) {
        if (allGroupHasNoProducts(element)) binding?.root?.hide()
        else bindBestSeller(element)
    }

    private fun allGroupHasNoProducts(element: BestSellerDataModel) =
        element.chipProductList.all { it.productModelList.isEmpty() }

    private fun bindBestSeller(element: BestSellerDataModel) {
        binding?.root?.show()

        bestSellerDataModel = element

        initHeader(element)
        initFilterChip(element)
        initCarouselPaging(element)
        setChannelDivider(element)
    }

    private fun initHeader(element: BestSellerDataModel){
        binding?.homeComponentHeaderBestSellerView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    val applink = chipsAdapter.currentList.find { it.isActivated }?.seeMoreApplink ?: ""

                    bestSellerListener.onBestSellerSeeMoreTextClick(element, applink)
                }

                override fun onChannelExpired(channelModel: ChannelModel) { }
            }
        )
    }

    private fun initFilterChip(element: BestSellerDataModel) {
        val recyclerView =
            binding?.homeComponentBestSellerChipsRecyclerView ?: return

        recyclerView.shouldShowWithAction(element.willShow) {
            chipsAdapter.submitList(element.chipProductList)

            scrollToActivatedChips(element.chipProductList)
        }
    }

    private fun scrollToActivatedChips(chipDataModelList: List<BestSellerChipProductDataModel>) {
        val activatedChipIndex = chipDataModelList.indexOfFirst { it.isActivated }

        binding?.homeComponentBestSellerChipsRecyclerView?.post {
            chipsLayoutManager.scrollToPositionWithOffset(activatedChipIndex, 0)
        }
    }

    private fun initCarouselPaging(element: BestSellerDataModel) {
        binding?.homeComponentBestSellerCarouselPaging?.setPagingModel(
            model = CarouselPagingModel(
                productCardGroupList = productCardGroupList(element),
                currentGroupPosition = element.activeChipPosition,
                currentPageInGroup = element.currentPageInGroup,
            ),
            listener = object: CarouselPagingProductCardView.CarouselPagingListener {
                override fun onGroupChanged(selectedGroupModel: CarouselPagingSelectedGroupModel) {
                    onGroupChanged(element, selectedGroupModel)
                }

                override fun onItemImpress(groupModel: CarouselPagingGroupModel, itemPosition: Int) {
                    onItemImpress(element, groupModel, itemPosition)
                }

                override fun onItemClick(groupModel: CarouselPagingGroupModel, itemPosition: Int) {
                    onItemClick(element, groupModel, itemPosition)
                }
            },
            recycledViewPool = recycledViewPool,
        )
    }

    private fun productCardGroupList(element: BestSellerDataModel) =
        element.chipProductList.map {
            CarouselPagingGroupProductModel(
                group = carouselPagingGroupModel(it),
                productItemList = it.productModelList.map(BestSellerProductDataModel::productCardModel),
            )
        }

    private fun carouselPagingGroupModel(chipProductDataModel: BestSellerChipProductDataModel) =
        CarouselPagingGroupModel(title = chipProductDataModel.title)

    private fun onGroupChanged(
        element: BestSellerDataModel,
        selectedGroupModel: CarouselPagingSelectedGroupModel
    ) {
        activateSelectedChip(selectedGroupModel.title)

        val chipPosition =
            element.chipProductList.indexOfFirst { it.title == selectedGroupModel.title }
        val chip = element.chipProductList.getOrNull(chipPosition)?.chip ?: return
        val bestSellerDataModel = bestSellerDataModel ?: return
        val activatedChipProduct = bestSellerDataModel.findChip(chip.title) ?: return

        bestSellerListener.onBestSellerFilterScrolled(
            activatedChipProduct,
            bestSellerDataModel,
            bindingAdapterPosition,
            chipPosition,
            selectedGroupModel.direction,
        )
    }

    private fun onItemImpress(
        element: BestSellerDataModel,
        groupModel: CarouselPagingGroupModel,
        itemPosition: Int,
    ) {
        val chipProductDataModel = element.chipProductList.find {
            it.chip.title == groupModel.title
        }
        val productDataModel =
            chipProductDataModel?.productModelList?.getOrNull(itemPosition) ?: return

        bestSellerListener.onBestSellerImpress(
            element,
            productDataModel,
            bindingAdapterPosition
        )
    }

    private fun onItemClick(
        element: BestSellerDataModel,
        groupModel: CarouselPagingGroupModel,
        itemPosition: Int
    ) {
        val chipProductDataModel = element.chipProductList.find {
            it.chip.title == groupModel.title
        }
        val productDataModel =
            chipProductDataModel?.productModelList?.getOrNull(itemPosition) ?: return

        bestSellerListener.onBestSellerClick(
            element,
            productDataModel,
            bindingAdapterPosition
        )
    }

    override fun onChipImpressed(chip: BestSellerChipDataModel) {
        val bestSellerDataModel = bestSellerDataModel ?: return

        bestSellerListener.onBestSellerFilterImpress(chip, bestSellerDataModel)
    }

    override fun onChipClicked(chip: BestSellerChipDataModel) {
        activateSelectedChip(chip.title)

        val bestSellerDataModel = bestSellerDataModel ?: return
        val activatedChipProduct = bestSellerDataModel.findChip(chip.title) ?: return

        binding?.homeComponentBestSellerCarouselPaging?.scrollToGroup(
            carouselPagingGroupModel(activatedChipProduct),
            CarouselPagingModel.FIRST_PAGE_IN_GROUP,
        )

        bestSellerListener.onBestSellerFilterClick(activatedChipProduct, bestSellerDataModel)
    }

    private fun activateSelectedChip(title: String) {
        val bestSellerDataModel = bestSellerDataModel ?: return
        val newChipList = bestSellerDataModel.chipProductList.map {
            val chip = if (it.title == title) it.chip.activate() else it.chip.deactivate()
            it.copy(chip = chip)
        }

        chipsAdapter.submitList(newChipList)

        scrollToActivatedChips(newChipList)
    }

    override fun bind(element: BestSellerDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: BestSellerDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerBestSellerHeader,
            dividerBottom = binding?.homeComponentDividerBestSellerFooter,
        )
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.homeComponentBestSellerCarouselPaging?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_best_seller
    }
}
