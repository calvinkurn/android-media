package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayUgcProductPickerBinding
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/08/22
 */
class PlayUGCProductPickerBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
) : BottomSheetDialogFragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val viewModel by viewModels<PlayBroadcastViewModel>(
        ownerProducer = { requireActivity() },
        factoryProducer = { parentViewModelFactoryCreator.create(requireActivity()) }
    )

    private val productTagListener = object : ProductTagParentFragment.Listener {
        override fun onCloseProductTag() {
            dismiss()
        }

        override fun onFinishProductTag(products: List<ProductUiModel>) {
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = BottomSheetPlayUgcProductPickerBinding.inflate(
            inflater,
            container,
            false
        ).root
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductTagParentFragment -> childFragment.setListener(productTagListener)
        }
    }

    fun showNow(fragmentManager: FragmentManager) {
        showNow(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        val selectedAccount = viewModel.uiState.value.selectedContentAccount

        val productPicker = ProductTagParentFragment.getFragment(
            fragmentManager = childFragmentManager,
            classLoader = requireActivity().classLoader,
            argumentBuilder = ContentProductTagArgument.Builder()
                .setAuthorType(selectedAccount.type)
                .setProductTagSource("play")
                .setAuthorId(selectedAccount.id)
                .setShopBadge(selectedAccount.badge)
                .setMultipleSelectionProduct(true),
        )

        childFragmentManager.beginTransaction()
            .replace(view.id, productPicker, ProductTagParentFragment.TAG)
            .commit()
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.peekHeight = getScreenHeight()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    companion object {
        private const val TAG = "PlayUGCProductPickerBottomSheet"

        fun get(fragmentManager: FragmentManager): PlayUGCProductPickerBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? PlayUGCProductPickerBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayUGCProductPickerBottomSheet {
            val existing = get(fragmentManager)
            if (existing != null) return existing

            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayUGCProductPickerBottomSheet::class.java.name
            ) as PlayUGCProductPickerBottomSheet
        }
    }
}