package com.tokopedia.salam.umrah.checkout.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutPilgrimsActivity
import com.tokopedia.salam.umrah.checkout.presentation.viewmodel.UmrahCheckoutPilgrimsViewModel
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.UmrahPilgrimsTitle
import com.tokopedia.salam.umrah.common.data.UmrahPilgrimsTitleType
import com.tokopedia.salam.umrah.common.util.CommonParam
import com.tokopedia.salam.umrah.common.util.DIGIT_STRING
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorian
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDateGregorianID
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getTime
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_umrah_checkout_pilgrims.*
import kotlinx.android.synthetic.main.widget_umrah_autocomplete_edit_text.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author by firman on 27/11/2019
 */


class UmrahCheckoutPilgrimsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var umrahCheckoutPilgrimsViewModel: UmrahCheckoutPilgrimsViewModel

    lateinit var pilgrimsData: UmrahCheckoutPilgrims
    var dateBirth = ""

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter


    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahCheckoutComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_checkout_pilgrims, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pilgrimsData = it.getParcelable(UmrahCheckoutPilgrimsActivity.EXTRA_PILGRIMS)
                    ?: UmrahCheckoutPilgrims()
        }
    }

    companion object {
        const val MAX_DAYS_BEFORE = 14
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
        umrahCheckoutPilgrimsViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahCheckoutPilgrimsViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
            contactList?.let { travelContactArrayAdapter.updateItem(it.toMutableList()) }
        })

    }

    private fun initView() {
        context?.let {
            initFirstNameAutoCompleteTv(it)
        }
        renderFilledUI()
        renderPilgrimsTitle()
        renderDatePicker()
        renderCheckForm()

        btn_umrah_checkout_pilgrims.setOnClickListener {
            if (validateData()) {
                pilgrimsData.title = getPessangerTitleShort(getPassengerTitle())
                pilgrimsData.firstName = ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.text.toString()
                pilgrimsData.lastName = tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.text.toString()
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

    private fun renderCheckForm() {
        ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 0) {
                    ac_umrah_autocomplete_first_name.til_umrah_autocomplete.error = ""
                }
            }

        })
        tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.setKeyListener(DigitsKeyListener.getInstance(DIGIT_STRING));
        tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 0) {
                    tf_umrah_checkout_pilgrims_contact_last_name.setMessage("")
                    tf_umrah_checkout_pilgrims_contact_last_name.setError(false)
                }else{
                    tf_umrah_checkout_pilgrims_contact_last_name.setError(true)
                }
            }

        })

        tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 0) {
                    tf_umrah_checkout_pilgrims_contact_date_birth.setMessage("")
                    tf_umrah_checkout_pilgrims_contact_date_birth.setError(false)
                }else{
                    tf_umrah_checkout_pilgrims_contact_date_birth.setError(true)
                }
            }

        })
    }

    private fun validateData(): Boolean {
        var isValid = true
        if (ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.text.isNullOrBlank()) {
            ac_umrah_autocomplete_first_name.til_umrah_autocomplete.error = getString(R.string.umrah_checkout_pilgrims_first_name_error)
            isValid = false
        }
        if (tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.text.isNullOrBlank()) {
            tf_umrah_checkout_pilgrims_contact_last_name.setMessage(getString(R.string.umrah_checkout_pilgrims_last_name_error))
            tf_umrah_checkout_pilgrims_contact_last_name.setError(true)
            isValid = false
        }
        if (tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.text.isNullOrBlank()) {
            tf_umrah_checkout_pilgrims_contact_date_birth.setError(true)
            tf_umrah_checkout_pilgrims_contact_date_birth.setMessage(getString(R.string.umrah_checkout_pilgrims_date_birth_error))
            isValid = false
        }

        if (getPassengerTitle().isNullOrBlank()) {
            view?.let {
                Toaster.build(it, getString(R.string.umrah_checkout_pilgrims_title_error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
            isValid = false
        }
        return isValid
    }

    private fun renderDatePicker() {
        val now = Calendar.getInstance()
        dateBirth = pilgrimsData.dateBirth

        context?.let {
            val datePickerUnify = DatePickerUnify(it, getLastTime(), now, getCalendarTwoWeeksBefore())
            datePickerUnify.setTitle(getString(R.string.umroh_checkout_birth_date))
            datePickerUnify.clearClose(false)
            tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.inputType = InputType.TYPE_NULL
            tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            datePickerUnify.show(activity!!.supportFragmentManager, "")
                        }
                        MotionEvent.ACTION_UP -> {
                            v?.performClick()
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            datePickerUnify.datePickerButton.setOnClickListener {
                tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.setText(getDateGregorianID(datePickerUnify.getDate(), UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT))
                dateBirth = getDateGregorian(datePickerUnify.getDate())
                datePickerUnify.dismiss()
            }

            datePickerUnify.setCloseClickListener {
                datePickerUnify.dismiss()
            }
        }
    }

    private fun renderPilgrimsTitle() {
        rv_umrah_checkout_pilgrims_title.listener = object : FilterChipAdapter.OnClickListener {
            override fun onChipClickListener(string: String, isSelected: Boolean) {
                if (isSelected) {
                    pilgrimsData.title = getPessangerTitleShort(string)
                }
            }
        }

        val entries = resources.getStringArray(R.array.umrah_checkout_pilgrims_titles)
        rv_umrah_checkout_pilgrims_title.setItem(ArrayList(Arrays.asList(*entries)),
                initialSelectedItemPos = if (pilgrimsData.title.isNotEmpty()) getPassengerTitleId(pilgrimsData.title) else null)

        rv_umrah_checkout_pilgrims_title.selectOnlyOneChip(true)
        rv_umrah_checkout_pilgrims_title.canDiselectAfterSelect(false)
    }

    private fun renderFilledUI() {
        ac_umrah_autocomplete_first_name.setLabel(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_first))
        ac_umrah_autocomplete_first_name.setHint(getString(R.string.umrah_checkout_bottom_sheet_pilgrims_label_name_first_label))

        if (pilgrimsData.dateBirth.isNotEmpty())
            tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.setText(getTime(UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT, pilgrimsData.dateBirth))
        if (pilgrimsData.firstName.isNotEmpty())
            ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.setText(pilgrimsData.firstName)
        if (pilgrimsData.lastName.isNotEmpty())
            tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.setText(pilgrimsData.lastName)

    }

    fun getPassengerTitle(): String = rv_umrah_checkout_pilgrims_title.getFirstSelectedItem()

    fun getPessangerTitleShort(passengerTitle: String): String {
        return if (passengerTitle.equals(UmrahPilgrimsTitle.TUAN, true)) UmrahPilgrimsTitle.TUAN_SHORT
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NYONYA, true)) UmrahPilgrimsTitle.NYONYA_SHORT
        else UmrahPilgrimsTitle.NONA_SHORT
    }

    fun getPassengerTitleId(passengerTitle: String): Int {
        return if (passengerTitle.equals(UmrahPilgrimsTitle.TUAN_SHORT, true)) UmrahPilgrimsTitleType.TUAN
        else if (passengerTitle.equals(UmrahPilgrimsTitle.NYONYA_SHORT, true)) UmrahPilgrimsTitleType.NYONYA
        else UmrahPilgrimsTitleType.NONA
    }

    private fun getLastTime(): Calendar {
        return GregorianCalendar(YEAR_END, MONTH_END, DAY_END)
    }

    private fun getCalendarTwoWeeksBefore(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -MAX_DAYS_BEFORE)
        return calendar
    }

    fun initFirstNameAutoCompleteTv(context: Context) {
        travelContactArrayAdapter = TravelContactArrayAdapter(context,
                com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv, arrayListOf(),
                object : TravelContactArrayAdapter.ContactArrayListener {
                    override fun getFilterText(): String {
                        return ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.text.toString()
                    }
                })
        (ac_umrah_autocomplete_first_name.ac_umrah_autocomplete as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
        (ac_umrah_autocomplete_first_name.ac_umrah_autocomplete as AutoCompleteTextView).onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> autofillPassengerContact(travelContactArrayAdapter.getItem(position)) }
    }

    fun autofillPassengerContact(contact: TravelContactListModel.Contact?) {
        clearAllFields()
        if (contact != null) {
            if (contact.firstName.isNotBlank()) {
                pilgrimsData.firstName = contact.firstName
                ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.setText(contact.firstName)
            }
            if (contact.lastName.isNotBlank()) {
                pilgrimsData.lastName = contact.lastName
                tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.setText(contact.lastName)
            }
            if (contact.title.isNotBlank()) {
                pilgrimsData.title = contact.shortTitle
                renderPassengerTitle(contact.title.toLowerCase())
            }
            if (contact.birthDate.isNotBlank()) {
                dateBirth = contact.birthDate
                tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.setText(getTime(UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT, contact.birthDate))
            }

        }
    }

    fun clearAllFields() {
        ac_umrah_autocomplete_first_name.ac_umrah_autocomplete.setText("")
        tf_umrah_checkout_pilgrims_contact_last_name.textFieldInput.setText("")
        tf_umrah_checkout_pilgrims_contact_date_birth.textFieldInput.setText("")
        rv_umrah_checkout_pilgrims_title.onResetChip()
    }

    private fun renderPassengerTitle(passengerTitle: String) {
        when {
            passengerTitle.equals(UmrahPilgrimsTitle.TUAN, true) -> rv_umrah_checkout_pilgrims_title.selectChipByPosition(0)
            passengerTitle.equals(UmrahPilgrimsTitle.NYONYA, true) -> rv_umrah_checkout_pilgrims_title.selectChipByPosition(1)
            passengerTitle.equals(UmrahPilgrimsTitle.NONA, true) -> rv_umrah_checkout_pilgrims_title.selectChipByPosition(2)
            else -> rv_umrah_checkout_pilgrims_title.onResetChip()
        }
    }


}