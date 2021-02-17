package com.tokopedia.localizationchooseaddress.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ChooseAddressWidget: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var textChosenAddress: Typography? = null
    private var buttonChooseAddress: IconUnify? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null

    private val chooseAddressWidgetListener: ChooseAddressWidgetListener? = null
    private val localCacheData: LocalCacheModel? = null

    interface ChooseAddressWidgetListener {
        fun onCheckLocalCache(state: Boolean)
        fun onGetChosenAddress(data: LocalCacheModel?)


        fun onUserChoosenAddress() // ini yang dari bottomshet dan UI harus implement
        fun onChoosenAddressUpdatedFromBackground() // ini yg background kita hit dan update cache, tapi UI optional untuk lakuin sesuatu. contoh pas non login cache kosong.
        fun onFeatureActive(acrtive: Boolean)
        /*
        gak perlu balikin data apapun. datanya nanti biar mereka tetep ambil dari util kita,
        misal ChooseAddressUtils.getLatestChoosenAddress()
        */
    }


    init {
        View.inflate(context, R.layout.choose_address_widget, this)
        initViews()
        initChooseAddressFlow()
    }

    private fun initViews() {
        chooseAddressPref = ChooseAddressSharePref(context)

        textChosenAddress = findViewById(R.id.text_chosen_address)
        buttonChooseAddress = findViewById(R.id.btn_arrow)

        checkRollence()
    }

    private fun checkRollence(){
        // check rollence. kalo udah panggil listener, biar host page yang atur show or hide
        chooseAddressWidgetListener.onFeatureActive(true)
    }

    public fun updateWidget(){
        textChosenAddress.setText("ambil dari local chace")
    }


    private fun initChooseAddressFlow() {

        if (chooseAddressPref?.checkLocalCache()?.isEmpty() == false) {
            val data = chooseAddressPref?.getLocalCacheData()
            textChosenAddress?.text = data?.label

        } else {
            //hit get local choose address, setelah selesai dan kita berhasil simpen di local cache
            chooseAddressWidgetListener.onChoosenAddressUpdatedFromBackground()
        }
    }

    fun setBindFragmentManager(fragment: FragmentManager) {
        buttonChooseAddress?.setOnClickListener {
            ChooseAddressBottomSheet().show(fragment)
        }
    }



/*    fun getLocalCacheData() {
        if (chooseAddressPref?.checkLocalCache()?.isNotEmpty() == true) {
            val data = chooseAddressPref?.getLocalCacheData()
            chooseAddressWidgetListener?.onGetChosenAddress(data)
        }
    }

    fun checkLocalCache() {
        if (chooseAddressPref?.checkLocalCache()?.isNotEmpty() == true) {
            chooseAddressWidgetListener?.onCheckLocalCache(true)
        } else chooseAddressWidgetListener?.onCheckLocalCache(false)
    }*/


}