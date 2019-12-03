package com.tokopedia.salam.umrah.checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutPilgrimsActivity
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.UmrahPilgrimsTitle
import com.tokopedia.salam.umrah.common.data.UmrahPilgrimsTitleType
import com.tokopedia.salam.umrah.common.util.CommonParam
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorianID
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorian
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDay
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_umrah_checkout_pilgrims.*
import java.util.*
import javax.inject.Inject


class UmrahCheckoutPilgrimsFragment : BaseDaggerFragment(){

    @Inject
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

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

        renderFilledUI()
        renderPilgrimsTitle()
        renderDatePicker()

        btn_umrah_checkout_pilgrims.setOnClickListener {
            if (validateData()) {
            pilgrimsData.title = getPessangerTitleShort(getPassengerTitle())
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
    }

    private fun validateData(): Boolean{
        var isValid = true
        if (til_umrah_checkout_pilgrims_contact_first_name.editText.text.isNullOrBlank()) {
            til_umrah_checkout_pilgrims_contact_first_name.error = getString(R.string.umrah_checkout_pilgrims_first_name_error)
            isValid = false
        }
        if (til_umrah_checkout_pilgrims_contact_last_name.editText.text.isNullOrBlank()) {
            til_umrah_checkout_pilgrims_contact_last_name.error = getString(R.string.umrah_checkout_pilgrims_last_name_error)
            isValid = false
        }
        if (til_umrah_checkout_pilgrims_contact_date_birth.editText.text.isNullOrBlank()) {
            til_umrah_checkout_pilgrims_contact_date_birth.error = getString(R.string.umrah_checkout_pilgrims_date_birth_error)
            isValid = false
        }

        if (getPassengerTitle().isNullOrBlank()){
            view?.let {
                Toaster.showError(it,getString(R.string.umrah_checkout_pilgrims_title_error)
                        ?: "", Snackbar.LENGTH_LONG)
            }
            isValid = false
        }
        return isValid
    }

    private fun renderDatePicker(){
        val now = Calendar.getInstance()
        dateBirth = pilgrimsData.dateBirth

        val datePickerUnify = DatePickerUnify(context!!,getLastTime(), now, now)
        datePickerUnify.setTitle(getString(R.string.umroh_checkout_birth_date))
        datePickerUnify.clearClose(false)

        til_umrah_checkout_pilgrims_contact_date_birth.editText.setOnClickListener{
            datePickerUnify.show(activity!!.supportFragmentManager,"")
        }

        datePickerUnify.datePickerButton.setOnClickListener {
            til_umrah_checkout_pilgrims_contact_date_birth.editText.setText(getDateGregorianID(datePickerUnify.getDate(), UmrahDateUtil.DATE_WITH_YEAR_FORMAT))
            dateBirth = getDateGregorian(datePickerUnify.getDate())
            datePickerUnify.dismiss()
        }

        datePickerUnify.setCloseClickListener {
            datePickerUnify.dismiss()
        }
    }

    private fun renderPilgrimsTitle(){
        rv_umrah_checkout__pilgrims_title.listener = object : FilterChipAdapter.OnClickListener {
            override fun onChipClickListener(string: String, isSelected: Boolean) {
                if (isSelected) {
                    pilgrimsData.title = getPessangerTitleShort(string)
                }
            }
        }

        val entries = resources.getStringArray(R.array.umrah_checkout_pilgrims_titles)
        rv_umrah_checkout__pilgrims_title.setItem(ArrayList(Arrays.asList(*entries)),
                initialSelectedItemPos = if ( pilgrimsData.title.isNotEmpty()) getPassengerTitleId(pilgrimsData.title) else null)

        rv_umrah_checkout__pilgrims_title.selectOnlyOneChip(true)
        rv_umrah_checkout__pilgrims_title.canDiselectAfterSelect(false)
    }

    private fun renderFilledUI(){
        til_umrah_checkout_pilgrims_contact_first_name.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_first))
        til_umrah_checkout_pilgrims_contact_last_name.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_last))
        til_umrah_checkout_pilgrims_contact_date_birth.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_date_birth))

        if (pilgrimsData.dateBirth.isNotEmpty())
            til_umrah_checkout_pilgrims_contact_date_birth.editText.setText(getDay(UmrahDateUtil.DATE_WITH_YEAR_FORMAT,pilgrimsData.dateBirth))
        if (pilgrimsData.firstName.isNotEmpty())
            til_umrah_checkout_pilgrims_contact_first_name.editText.setText(pilgrimsData.firstName)
        if (pilgrimsData.lastName.isNotEmpty())
            til_umrah_checkout_pilgrims_contact_last_name.editText.setText(pilgrimsData.lastName)

    }

    fun getPassengerTitle(): String = rv_umrah_checkout__pilgrims_title.getFirstSelectedItem()

    fun getPessangerTitleShort(passengerTitle: String): String{
        return if (passengerTitle.equals(UmrahPilgrimsTitle.TUAN, true)) UmrahPilgrimsTitle.TUAN_SHORT
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NYONYA, true)) UmrahPilgrimsTitle.NYONYA_SHORT
        else UmrahPilgrimsTitle.NONA_SHORT
    }

    fun getPassengerTitleId(passengerTitle: String): Int {
        return if (passengerTitle.equals(UmrahPilgrimsTitle.TUAN_SHORT, true)) UmrahPilgrimsTitleType.TUAN
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NYONYA_SHORT, true)) UmrahPilgrimsTitleType.NYONYA
        else UmrahPilgrimsTitleType.NONA
    }

    private fun getLastTime(): Calendar{
        val calendar = GregorianCalendar(YEAR_END, MONTH_END, DAY_END)
        return calendar
    }

    private fun renderPassengerTitle(passengerTitle: String) {
        if (passengerTitle.equals(UmrahPilgrimsTitle.TUAN_SHORT, true))
            rv_umrah_checkout__pilgrims_title.selectChipByPosition(0)
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NYONYA_SHORT, true))
            rv_umrah_checkout__pilgrims_title.selectChipByPosition(1)
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NONA_SHORT, true))
            rv_umrah_checkout__pilgrims_title.selectChipByPosition(2)
        else rv_umrah_checkout__pilgrims_title.onResetChip()
    }

}