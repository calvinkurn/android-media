package com.tokopedia.stories.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by astidhiyaa on 25/07/23
 */
class StoriesProductBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : BottomSheetUnify() {

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composable = ComposeView(requireContext()).apply {
            setContent {
                val ctx = LocalContext.current
                LaunchedEffect(Unit) {
                    //observe  event
                }
                ProductPage(products = emptyList(),
                    onCardClicked = { item, pos -> },
                    onBuyClicked = { item, pos -> },
                    onAtcClicked = { item, pos -> })
                /**
                 *
                 *
                 * when (val result = viewModel?.feedTagProductList?.observeAsState()?.value) {
                is Success -> ProductPage(
                result.data,
                onCardClicked = { item, pos -> },
                onBuyClicked = { item, pos -> },
                onAtcClicked = { item, pos -> })
                else -> {}
                }
                 */
            }
        }
        setChild(composable)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, StoriesThreeDotsBottomSheet.TAG)
    }

    override fun dismiss() {
        if (!isAdded) return
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Product))
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Product))
        super.onCancel(dialog)
    }

    companion object {
        const val TAG = "StoriesProductBottomSheet"

        fun get(fragmentManager: FragmentManager): StoriesProductBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? StoriesProductBottomSheet
        }

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesProductBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesProductBottomSheet::class.java.name
            ) as StoriesProductBottomSheet
        }
    }
}
