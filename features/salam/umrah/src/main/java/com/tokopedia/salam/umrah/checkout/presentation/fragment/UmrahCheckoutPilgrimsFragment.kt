package com.tokopedia.salam.umrah.checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutPilgrimsActivity
import com.tokopedia.salam.umrah.common.util.CommonParam
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorianID
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorian
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDay
import kotlinx.android.synthetic.main.fragment_umrah_checkout_pilgrims.*
import java.util.*



class UmrahCheckoutPilgrimsFragment : BaseDaggerFragment(){

    lateinit var pilgrimsData : UmrahCheckoutPilgrims
    var dateBirth = ""

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahCheckoutComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_checkout_pilgrims, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pilgrimsData = it.getParcelable(UmrahCheckoutPilgrimsActivity.EXTRA_PILGRIMS) ?: UmrahCheckoutPilgrims()
        }
    }

    companion object{
        const val YEAR_END = 1900
        const val DAY_END = 1
        const val MONTH_END = 1
        fun createInstance(dataPilgrims: UmrahCheckoutPilgrims): UmrahCheckoutPilgrimsFragment = UmrahCheckoutPilgrimsFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(UmrahCheckoutPilgrimsActivity.EXTRA_PILGRIMS, dataPilgrims)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        val now = Calendar.getInstance()

        til_umrah_checkout_pilgrims_contact_first_name.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_first))
        til_umrah_checkout_pilgrims_contact_last_name.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_last))

        renderFilledUI()
        dateBirth = pilgrimsData.dateBirth

        val datePickerUnify = DatePickerUnify(context!!,getLastTime(), now, now)
        datePickerUnify.setTitle(getString(R.string.umroh_checkout_birth_date))
        datePickerUnify.clearClose(false)

        et_umrah_checkout_pilgrims_date_birth.setOnClickListener{
            datePickerUnify.show(activity!!.supportFragmentManager,"")
        }

        datePickerUnify.datePickerButton.setOnClickListener {
            et_umrah_checkout_pilgrims_date_birth.setText(getDateGregorianID(datePickerUnify.getDate(),"dd MMMM YYYY"))
            dateBirth = getDateGregorian(datePickerUnify.getDate())
            datePickerUnify.dismiss()
        }

        datePickerUnify.setCloseClickListener {
            datePickerUnify.dismiss()
        }

        btn_umrah_checkout_pilgrims.setOnClickListener {
            pilgrimsData.title = sp_umrah_checkout__pilgrims_title_label.selectedItem.toString()
            pilgrimsData.firstName = ac_umrah_checkout_pilgrims_contact_first_name.text.toString()
            pilgrimsData.lastName = ac_umrah_checkout_pilgrims_contact_last_name.text.toString()
            pilgrimsData.dateBirth = dateBirth
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    val cacheManager = SaveInstanceCacheManager(this@run, true).also {
                        it.put(CommonParam.ARG_CHECKOUT, pilgrimsData)
                    }
                    putExtra(CommonParam.ARG_CHECKOUT_ID, cacheManager.id)
                })
                finish()
            }
        }
    }

    private fun renderFilledUI(){
        if (pilgrimsData.dateBirth.isNotEmpty())
            et_umrah_checkout_pilgrims_date_birth.setText(getDay("dd MMMM YYYY",pilgrimsData.dateBirth))
        if (pilgrimsData.firstName.isNotEmpty())
            til_umrah_checkout_pilgrims_contact_first_name.editText.setText(pilgrimsData.firstName)
        if (pilgrimsData.lastName.isNotEmpty())
            til_umrah_checkout_pilgrims_contact_last_name.editText.setText(pilgrimsData.lastName)
    }

    private fun getLastTime(): Calendar{
        val calendar = GregorianCalendar(YEAR_END, MONTH_END, DAY_END)
        return calendar
    }

}