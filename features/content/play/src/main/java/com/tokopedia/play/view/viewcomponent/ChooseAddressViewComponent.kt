package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.view_play_widget_address) {

    private lateinit var chooseAddressBottomSheet: ChooseAddressBottomSheet
    private val btnChoose: UnifyButton = findViewById(R.id.btn_change_address)

    private val insideListener = object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener{
        override fun onLocalizingAddressServerDown() {}

        override fun onAddressDataChanged() {
            listener.onAddressUpdated(this@ChooseAddressViewComponent)
        }

        override fun getLocalizingAddressHostSourceBottomSheet(): String = "login"

        override fun onLocalizingAddressLoginSuccessBottomSheet() {
            //TODO("Not yet implemented")
        }

        override fun onDismissChooseAddressBottomSheet() {
            //TODO("Not yet implemented")
        }
    }

    init {
        btnChoose.setOnClickListener {
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
        if (!getBottomSheet().isVisible)
            getBottomSheet().showNow(listener.getFragmentForAddress(this@ChooseAddressViewComponent).childFragmentManager, "")
    }

    private fun getBottomSheet() : ChooseAddressBottomSheet {
        if(!::chooseAddressBottomSheet.isInitialized) {
            chooseAddressBottomSheet = ChooseAddressBottomSheet()
            chooseAddressBottomSheet.setListener(insideListener)
        }
        return chooseAddressBottomSheet
    }

    interface Listener {
        fun getFragmentForAddress(view: ChooseAddressViewComponent) : Fragment
        fun onAddressUpdated(view: ChooseAddressViewComponent)
    }
}