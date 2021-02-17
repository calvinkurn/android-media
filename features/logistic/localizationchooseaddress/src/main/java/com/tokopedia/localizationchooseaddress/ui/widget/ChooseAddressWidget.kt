package com.tokopedia.localizationchooseaddress.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChooseAddressWidget: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var textChosenAddress: Typography? = null
    private var buttonChooseAddress: IconUnify? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ChooseAddressViewModel

    private val chooseAddressWidgetListener: ChooseAddressWidgetListener? = null
    private val localCacheData: LocalCacheModel? = null

    interface ChooseAddressWidgetListener {
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
    }

    private fun initViews() {
        chooseAddressPref = ChooseAddressSharePref(context)

        textChosenAddress = findViewById(R.id.text_chosen_address)
        buttonChooseAddress = findViewById(R.id.btn_arrow)

        checkRollence()
    }

    private fun initInjector() {
        getComponent().inject(this)
    }

    private fun initObservers() {
        viewModel.test.observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    Toast.makeText(context, it.data, Toast.LENGTH_LONG).show()
                }
            }

        })

       /* viewModel.chosenAddressList.observeForever( Observer {

        })*/
    }

/*    fun onDetach() {
        viewModel.chosenAddressList.removeObserver(this)
    }*/

    private fun checkRollence(){
        // check rollence. kalo udah panggil listener, biar host page yang atur show or hide
        chooseAddressWidgetListener?.onFeatureActive(true)
    }

    fun updateWidget(){
        textChosenAddress?.text = "ambil dari local chace"
    }

    fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun initChooseAddressFlow() {

        if (chooseAddressPref?.checkLocalCache()?.isEmpty() == false) {
            val data = chooseAddressPref?.getLocalCacheData()
            textChosenAddress?.text = data?.label

        } else {
            textChosenAddress?.text = "Pilih alamat pengiriman"
            //hit viewmodel
            viewModel.getChosenAddressList()
            //hit get local choose address, setelah selesai dan kita berhasil simpen di local cache
            chooseAddressWidgetListener?.onChoosenAddressUpdatedFromBackground()
        }
    }

    fun setBindFragmentManager(fm: FragmentManager, fragment: Fragment) {
        initInjector()
        viewModel = ViewModelProviders.of(fragment, viewModelFactory)[ChooseAddressViewModel::class.java]
        initChooseAddressFlow()
        initObservers()
        buttonChooseAddress?.setOnClickListener {
            ChooseAddressBottomSheet().show(fm)
        }
    }

}