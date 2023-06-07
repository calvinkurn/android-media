package com.tokopedia.product.detail.postatc.view.component.addons

import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.product.detail.databinding.ItemPostAtcAddonsBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class AddonsViewHolder(
    private val binding: ItemPostAtcAddonsBinding,
    private val callback: PostAtcCallback
) : PostAtcViewHolder<AddonsUiModel>(binding.root), AddOnComponentListener {

    private var element: AddonsUiModel? = null

    init {
        binding.postAtcAddonsWidget.setListener(this)
    }

    override fun bind(element: AddonsUiModel) = with(binding.postAtcAddonsWidget) {
        val data = element.data
        setSelectedAddons(data.selectedAddonsIds) // Possible bug if it set several times.
        setTitleText(data.title)
        getAddonData(data.productId, data.warehouseId, data.isTokoCabang)
        this@AddonsViewHolder.element = element
    }

    override fun onAddonComponentError(throwable: Throwable) {
        val element = this.element ?: return
        callback.removeComponent(element.id)
    }

    override fun onAddonComponentClick(
        index: Int,
        indexChild: Int,
        addOnGroupUIModels: List<AddOnGroupUIModel>
    ) {
        /**
         * TODO vindo - tracker use?
         */
    }

    override fun onDataEmpty() {
        val element = this.element ?: return
        callback.removeComponent(element.id)
    }

    override fun onSaveAddonLoading() {
        callback.onLoadingSaveAddons()
        super.onSaveAddonLoading()
    }

    override fun onSaveAddonFailed(throwable: Throwable) {
        callback.onFailedSaveAddons("Ada gangguan yang lagi dibereskan. Coba lagi atau balik lagi nanti, ya.")
        super.onSaveAddonFailed(throwable)
    }

    override fun onSaveAddonSuccess(
        selectedAddonIds: List<String>,
        selectedAddons: List<AddOnUIModel>,
        selectedAddonGroup: List<AddOnGroupUIModel>
    ) {
        callback.onSuccessSaveAddons(selectedAddonIds.size)
        super.onSaveAddonSuccess(selectedAddonIds, selectedAddons, selectedAddonGroup)
    }

}
