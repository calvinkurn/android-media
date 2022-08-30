package com.tokopedia.tokopedianow.recipedetail.presentation.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.common.view.ToolbarHeaderView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeDetailViewModel

class RecipeChooseAddressListener(
    private val toolbarHeader: ToolbarHeaderView?,
    private val recyclerView: RecyclerView?,
    private val viewModel: TokoNowRecipeDetailViewModel
): ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {

    companion object {
        private const val CHANGE_ADDRESS_SOURCE = "tokonow"
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String =
        CHANGE_ADDRESS_SOURCE

    override fun onAddressDataChanged() {
        removeScrollListener()
        toolbarHeader?.reset()
        viewModel.refreshPage()
    }

    override fun onLocalizingAddressServerDown() { /* to do : nothing */
    }

    override fun onLocalizingAddressLoginSuccessBottomSheet() { /* to do : nothing */
    }

    override fun onDismissChooseAddressBottomSheet() { /* to do : nothing */
    }

    private fun removeScrollListener() {
        toolbarHeader?.scrollListener?.let {
            recyclerView?.removeOnScrollListener(it)
        }
    }
}