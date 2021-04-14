package com.tokopedia.age_restriction.viewcontroller

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.age_restriction.R
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.track.TrackApp
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import kotlinx.android.synthetic.main.layout_activity_dob.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class VerifyDOBActivity : BaseARActivity<VerifyDOBViewModel>() {

    private var selectedDate: Date? = null
    private var travelCalenderSheet: TravelCalendarBottomSheet? = null
    private lateinit var verifyDobModel: VerifyDOBViewModel

    private var now = Calendar.getInstance()

    private var endDate: Date? = null

    private var startdate: Date? = null

    @Inject
    lateinit var viewModelProvider:  ViewModelProvider.Factory

    override fun getVMFactory(): ViewModelProvider.Factory? {
       return viewModelProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    override fun getViewModelType(): Class<VerifyDOBViewModel> {
        return VerifyDOBViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
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
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            if (selectedDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = selectedDate

                verifyDobModel.updateUserDoB(
                        calendar.get(Calendar.DATE).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.YEAR).toString())
            } else {
                showMessage("Silakan pilih tanggal yang valid")
            }

            sendGeneralEvent(eventClick,
                    event,
                    "click - adult pop up - simpan",
                    "tanggal lahir page - " + simpleDateFormat.format(selectedDate) + " - $destinationUrlGtm - $event/$destinationUrlGtm")
        }
    }

    protected fun sendGeneralEvent(event: String, category: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event,
                category,
                action,
                label)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group.visibility = View.INVISIBLE
        if (intent.hasExtra(PARAM_EXTRA_DOB)) {
            val dob = intent.getStringExtra(PARAM_EXTRA_DOB)?.split("-")
            dob?.let {
                if (it.size == 3) {
                    verifyDobModel.updateUserDoB(
                            it[2],
                            it[1],
                            it[0])
                }
            }
        } else {
            group.visibility = View.VISIBLE
            changeButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_N100)
            tv_update_dob.isClickable = false
            tv_update_dob.isFocusable = false
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

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_activity_dob
    }

    private fun changeButtonColor(id : Int) {
        val shapeDrawable = tv_update_dob.background as GradientDrawable
        shapeDrawable.setColor(ContextCompat.getColor(this, id))
        tv_update_dob.background = shapeDrawable
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
                    ed_edit_dob.text = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID")).format(dateSelected)
                    selectedDate = dateSelected
                    hideProgressBar()
                    ed_edit_dob.isClickable = true
                    iv_arrow_down.isClickable = true
                    changeButtonColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
                    tv_update_dob.isClickable = true
                    tv_update_dob.isFocusable = true
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



    override fun showProgressBar() {
        progress_bar_layout.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar_layout.visibility = View.GONE
    }
}