package com.tokopedia.product.detail.postatc.view.component.addons

import com.tokopedia.addon.presentation.listener.AddOnComponentListener
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.databinding.ItemPostAtcAddonsBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class AddonsViewHolder(
    private val binding: ItemPostAtcAddonsBinding,
    private val callback: PostAtcCallback
) : PostAtcViewHolder<AddonsUiModel>(binding.root), AddOnComponentListener {

    private var element: AddonsUiModel? = null

    private var dataHash = -1

    override fun bind(element: AddonsUiModel) = with(binding.postAtcAddonsWidget) {
        val data = element.data
        if (dataHash != data.hashCode()) {
            setSelectedAddons(data.selectedAddonsIds)
            setTitleText(data.title)
            setAutosaveAddon(data.cartId.toLongOrZero(), "normal")
            getAddonData(data.productId, data.warehouseId, data.isTokoCabang)
            binding.postAtcAddonsWidget.setListener(this@AddonsViewHolder)
            dataHash = data.hashCode()
        }
        this@AddonsViewHolder.element = element
    }

    override fun onAddonComponentError(errorMessage: String) {
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

    override fun onSaveAddonFailed(errorMessage: String) {
        callback.onFailedSaveAddons(errorMessage)
        super.onSaveAddonFailed(errorMessage)
    }

    override fun onSaveAddonSuccess(
        selectedAddonIds: List<String>,
        changedAddonSelections: List<AddOnUIModel>,
        addonGroups: List<AddOnGroupUIModel>
    ) {
        callback.onSuccessSaveAddons(selectedAddonIds.size)
        super.onSaveAddonSuccess(selectedAddonIds, changedAddonSelections, addonGroups)
    }

}
