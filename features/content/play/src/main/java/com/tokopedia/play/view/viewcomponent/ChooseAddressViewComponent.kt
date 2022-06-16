package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener,
    private val fragmentManager: FragmentManager,
) : ViewComponent(container, R.id.view_play_widget_address) {

    private lateinit var chooseAddressBottomSheet: ChooseAddressBottomSheet
    private val btnChoose: UnifyButton = findViewById(R.id.btn_change_address)
    private val tvInfo: TextView = findViewById(R.id.tv_address_desc)

    private val insideListener = object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener{
        override fun onLocalizingAddressServerDown() {}

        override fun onAddressDataChanged() {
            listener.onAddressUpdated(this@ChooseAddressViewComponent)
        }

        override fun getLocalizingAddressHostSourceBottomSheet(): String = ADDRESS_WIDGET_SOURCE

        override fun onLocalizingAddressLoginSuccessBottomSheet() {}

        override fun onDismissChooseAddressBottomSheet() {
            hideBottomSheet()
        }
    }

    init {
        btnChoose.setOnClickListener {
            openBottomSheet()
        }

        tvInfo.text = MethodChecker.fromHtml(getString(R.string.play_address_widget_info))
        tvInfo.setOnClickListener {
            listener.onInfoClicked(this@ChooseAddressViewComponent)
        }
    }

    private fun openBottomSheet() {
        if (!getBottomSheet().isAdded)
            getBottomSheet().showNow(fragmentManager, PLAY_CHOOSE_ADDRESS_TAG)
    }

    private fun hideBottomSheet() {
        if (getBottomSheet().isAdded)
            getBottomSheet().dismiss()
    }

    private fun getBottomSheet() : ChooseAddressBottomSheet {
        if(!::chooseAddressBottomSheet.isInitialized) {
            chooseAddressBottomSheet = ChooseAddressBottomSheet()
            chooseAddressBottomSheet.setListener(insideListener)
        }
        return chooseAddressBottomSheet
    }

    companion object {
        private const val PLAY_CHOOSE_ADDRESS_TAG = "PLAY_ADDRESS"
        private const val ADDRESS_WIDGET_SOURCE = "play"
    }

    interface Listener {
        fun onAddressUpdated(view: ChooseAddressViewComponent)
        fun onInfoClicked(view: ChooseAddressViewComponent)
    }
}