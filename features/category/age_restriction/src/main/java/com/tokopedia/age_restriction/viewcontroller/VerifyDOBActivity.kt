package com.tokopedia.age_restriction.viewcontroller

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.age_restriction.R
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import kotlinx.android.synthetic.main.layout_activity_dob.*
import java.text.SimpleDateFormat
import java.util.*


class VerifyDOBActivity : BaseARActivity<VerifyDOBViewModel>() {

    private var selectedDate: Date? = null
    private var travelCalenderSheet: TravelCalendarBottomSheet? = null
    private lateinit var verifyDobModel: VerifyDOBViewModel

    private var now = Calendar.getInstance()

    private var endDate: Date? = null

    private var startdate: Date? = null

    override fun getViewModelType(): Class<VerifyDOBViewModel> {
        return VerifyDOBViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        verifyDobModel = viewModel as VerifyDOBViewModel
    }

    override fun initView() {
        super.initView()

        now = Calendar.getInstance(Locale("in", "ID"))
        now.add(Calendar.YEAR, -80)
        startdate = now.time
        now = Calendar.getInstance()
        endDate = now.time

        ed_edit_dob.setOnClickListener(clickListenerCalender)

        iv_arrow_down.setOnClickListener(clickListenerCalender)

        tv_update_dob.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat.getDateInstance()
            if (selectedDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = selectedDate

                verifyDobModel.updateUserDoB(getMeGQlString(R.raw.gql_user_profile_dob_update),
                        calendar.get(Calendar.DATE).toString(),
                        (calendar.get(Calendar.MONTH)+1).toString(),
                        calendar.get(Calendar.YEAR).toString())
            } else {
                showMessage("Silakan pilih tanggal yang valid")
            }

            sendGeneralEvent(eventClick,
                    event,
                    "click - adult pop up - simpan",
                    "tanggal lahir page - " + simpleDateFormat.format(selectedDate) + " - $event/$destinationUrlGtm")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group.visibility = View.INVISIBLE
        if (intent.hasExtra(PARAM_EXTRA_DOB)) {
            val dob = intent.getStringExtra(PARAM_EXTRA_DOB)?.split("-")
            dob?.let {
                if (it.size == 3) {
                    verifyDobModel.updateUserDoB(getMeGQlString(R.raw.gql_user_profile_dob_update),
                            it[2],
                            it[1],
                            it[0])
                }
            }
        } else {
            group.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        sendGeneralEvent(eventView,
                event,
                "view - adult pop up - tanggal lahir page",
                "tanggal lahir page - $event/$destinationUrlGtm")
        verifyDobModel.userIsAdult.observe(this, Observer {
            setResult(RESULT_IS_ADULT)
            finish()
        })

        verifyDobModel.userNotAdult.observe(this, Observer {
            setResult(RESULT_IS_NOT_ADULT)
            finish()
        })
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getTncFragmentInstance(TncResId: Int): Fragment? {
        return null
    }

    override fun getBottomSheetLayoutRes(): Int {
        return -1
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_activity_dob
    }

    private var clickListenerCalender = View.OnClickListener {

        showProgressBar()
        ed_edit_dob.isClickable = false
        iv_arrow_down.isClickable = false
        if (travelCalenderSheet == null) {
            travelCalenderSheet = TravelCalendarBottomSheet.Builder()
                    .setTitle(getString(R.string.ar_text_select_dob))
                    .setMinDate(startdate!!)
                    .setMaxDate(endDate)
                    .setSelectedDate(endDate!!)
                    .build()
            travelCalenderSheet?.setListener(object : TravelCalendarBottomSheet.ActionListener {
                override fun onClickDate(dateSelected: Date) {
                    ed_edit_dob.text = SimpleDateFormat("dd MMMMM YYYY",Locale("in","ID")).format(dateSelected)
                    selectedDate = dateSelected
                    hideProgressBar()
                    ed_edit_dob.isClickable = true
                    iv_arrow_down.isClickable = true
                }
            })
            travelCalenderSheet?.setDismissListener {
                hideProgressBar()
                ed_edit_dob.isClickable = true
                iv_arrow_down.isClickable = true
            }
        }

        if (!travelCalenderSheet!!.isVisible) {
            travelCalenderSheet!!.show(supportFragmentManager, "CalendarBottomSheet")
        }
    }
}