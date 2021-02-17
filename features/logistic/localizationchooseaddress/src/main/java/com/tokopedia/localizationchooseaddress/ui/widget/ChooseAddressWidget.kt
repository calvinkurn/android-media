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
    }

    private fun initChooseAddressFlow() {

    }

    fun setBindFragmentManager(fragment: FragmentManager) {
        buttonChooseAddress?.setOnClickListener {
            ChooseAddressBottomSheet().show(fragment)
        }
    }

 /*   fun getLocalCacheData() {
        if (chooseAddressPref?.checkLocalCache()?.isNotEmpty() == true) {
            val data = chooseAddressPref?.getLocalCacheData()
            chooseAddressWidgetListener?.onGetChosenAddress(data)
        }
    }*/

    fun checkLocalCache() {
        if (chooseAddressPref?.checkLocalCache()?.isNotEmpty() == true) {
            chooseAddressWidgetListener?.onCheckLocalCache(true)
        } else chooseAddressWidgetListener?.onCheckLocalCache(false)
    }


}