package com.tokopedia.pdpsimulation.activateCheckout.helper

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment.BottomSheetType
import com.tokopedia.unifycomponents.Toaster

object ActivationHelper {

    fun navigateToBottomSheet(
        bottomSheetType: BottomSheetType,
        childFragmentManager: FragmentManager,
        onDismissBottomSheet: () -> Unit
    ) {
        when (bottomSheetType) {
            is BottomSheetType.GateWayBottomSheet -> {
                SelectGateWayBottomSheet.show(bottomSheetType.bundleData, childFragmentManager)
                    .setOnDismissListener {
                        onDismissBottomSheet
                    }
            }
        }
    }


    fun View.showToaster(message: String?) {
        message?.let {
            Toaster.build(
                this,
                it,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }
}