package com.tokopedia.shop.settings.basicinfo.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopShedulePresenter
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.common.widget.ImageLabelView

import java.util.Calendar
import java.util.Date

import javax.inject.Inject

class ShopEditScheduleActivity : BaseSimpleActivity(), UpdateShopShedulePresenter.View {

    @Inject
    lateinit var  updateShopShedulePresenter: UpdateShopShedulePresenter

    private var progressDialog: ProgressDialog? = null

    private var tvSave: TextView? = null
    private var tilShopCloseNote: TkpdHintTextInputLayout? = null
    private var etShopCloseNote: EditText? = null
    private var labelStartClose: ImageLabelView? = null
    private var labelEndClose: ImageLabelView? = null

    private var selectedStartCloseUnixTimeMs: Long = 0
    private var selectedEndCloseUnixTimeMs: Long = 0

    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var isClosedNow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(this)

        shopBasicDataModel = intent.getParcelableExtra(EXTRA_SHOP_MODEL)
        val title = intent.getStringExtra(EXTRA_TITLE)
        isClosedNow = intent.getBooleanExtra(EXTRA_IS_CLOSED_NOW, false)
        setTitle(title)

        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE)
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE)
        } else {
            val closeSchedule = shopBasicDataModel!!.closeSchedule
            if (!StringUtils.isEmptyNumber(closeSchedule)) {
                selectedStartCloseUnixTimeMs = java.lang.Long.parseLong(closeSchedule) * 1000L
            }
            val closedUntil = shopBasicDataModel!!.closeUntil
            if (!StringUtils.isEmptyNumber(closedUntil)) {
                selectedEndCloseUnixTimeMs = java.lang.Long.parseLong(closedUntil) * 1000L
            }
        }

        super.onCreate(savedInstanceState)

        DaggerShopSettingsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        updateShopShedulePresenter!!.attachView(this)

        tvSave = findViewById(R.id.tvSave)
        labelStartClose = findViewById(R.id.labelStartClose)
        labelEndClose = findViewById(R.id.labelEndClose)
        tilShopCloseNote = findViewById(R.id.tilShopCloseNote)
        etShopCloseNote = findViewById(R.id.etShopCloseNote)
        etShopCloseNote!!.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                tilShopCloseNote!!.error = null
            }
        })

        setUIShopSchedule(shopBasicDataModel!!)

        labelStartClose!!.setOnClickListener {
            val minDate = tomorrowDate
            val selectedDate = unixToDate(selectedStartCloseUnixTimeMs)
            showStartDatePickerDialog(selectedDate, minDate)
        }
        labelEndClose!!.setOnClickListener {
            val minDate: Date
            if (selectedStartCloseUnixTimeMs == 0L) {
                if (isClosedNow) {
                    minDate = currentDate
                } else {
                    minDate = tomorrowDate
                }
            } else {
                minDate = unixToDate(selectedStartCloseUnixTimeMs)
            }
            val selectedDate: Date
            if (selectedEndCloseUnixTimeMs == 0L) {
                if (isClosedNow) {
                    selectedDate = currentDate
                } else {
                    selectedDate = tomorrowDate
                }
            } else {
                selectedDate = unixToDate(selectedEndCloseUnixTimeMs)
            }
            showEndDatePickerDialog(selectedDate, minDate)
        }
        tvSave!!.visibility = View.VISIBLE
        tvSave!!.setOnClickListener { onSaveButtonClicked() }
    }

    fun showStartDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            setStartCloseDate(toDate(year, month, dayOfMonth))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker.show()
    }

    fun showEndDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = toDate(year, month, dayOfMonth)
            setEndCloseDate(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker.show()
    }

    private fun setStartCloseDate(date: Date) {
        selectedStartCloseUnixTimeMs = date.time
        labelStartClose!!.setContent(toReadableString(FORMAT_DAY_DATE, date))
        // move end date to start date, if the end < start
        if (selectedEndCloseUnixTimeMs > 0 && selectedEndCloseUnixTimeMs < selectedStartCloseUnixTimeMs) {
            setEndCloseDate(Date(selectedStartCloseUnixTimeMs))
        }
    }

    private fun setEndCloseDate(date: Date) {
        selectedEndCloseUnixTimeMs = date.time
        labelEndClose!!.setContent(toReadableString(FORMAT_DAY_DATE, date))
    }

    private fun onSaveButtonClicked() {
        val closeNote = etShopCloseNote!!.text.toString()
        if (TextUtils.isEmpty(closeNote)) {
            tilShopCloseNote!!.error = getString(R.string.note_must_be_filled)
            return
        }

        showSubmitLoading(getString(R.string.title_loading))
        @ShopScheduleActionDef val shopAction = if (isClosedNow || shopBasicDataModel!!.isClosed)
            ShopScheduleActionDef.CLOSED
        else
            ShopScheduleActionDef.OPEN
        val closeStart = selectedStartCloseUnixTimeMs
        val closeEnd = selectedEndCloseUnixTimeMs
        updateShopShedulePresenter!!.updateShopSchedule(
                shopAction,
                isClosedNow,
                if (closeStart == 0L) null else closeStart.toString(),
                if (closeEnd == 0L) null else closeEnd.toString(),
                closeNote)
    }

    fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog!!.setMessage(message)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun hideSubmitLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (updateShopShedulePresenter != null) {
            updateShopShedulePresenter!!.detachView()
        }
    }

    override fun onSuccessUpdateShopSchedule(successMessage: String) {
        hideSubmitLoading()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onErrorUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    private fun setUIShopSchedule(shopBasicDataModel: ShopBasicDataModel) {
        val shopCloseSchedule = shopBasicDataModel.closeSchedule
        val shopCloseUntil = shopBasicDataModel.closeUntil

        val hasCloseUntil = !StringUtils.isEmptyNumber(shopCloseUntil)
        //set close schedule
        if (isClosedNow || shopBasicDataModel.isClosed) {
            labelStartClose!!.isEnabled = false
            setStartCloseDate(currentDate)
        } else {
            labelStartClose!!.isEnabled = true
            val hasCloseSchedule = !StringUtils.isEmptyNumber(shopCloseSchedule)
            if (hasCloseSchedule) {
                val shopCloseScheduleUnixMs = java.lang.Long.parseLong(shopCloseSchedule) * 1000L
                val shopCloseDate = unixToDate(shopCloseScheduleUnixMs)
                setStartCloseDate(shopCloseDate)
            }
        }

        //set open schedule.
        if (hasCloseUntil) {
            val shopOpenScheduleUnixMs = java.lang.Long.parseLong(shopCloseUntil) * 1000L
            val shopOpenDate = unixToDate(shopOpenScheduleUnixMs)
            setEndCloseDate(shopOpenDate)
        }
        etShopCloseNote!!.setText(shopBasicDataModel.closeNote)
    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable)
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again)) { onSaveButtonClicked() }.show()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SAVED_SELECTED_START_DATE, selectedStartCloseUnixTimeMs)
        outState.putLong(SAVED_SELECTED_END_DATE, selectedEndCloseUnixTimeMs)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_edit_schedule
    }

    companion object {

        val EXTRA_SHOP_MODEL = "shop_model"
        val EXTRA_TITLE = "title"
        val EXTRA_IS_CLOSED_NOW = "is_closed_now"

        val SAVED_SELECTED_START_DATE = "svd_selected_start_date"
        val SAVED_SELECTED_END_DATE = "svd_selected_end_date"

        @JvmStatic
        fun createIntent(context: Context, shopBasicDataModel: ShopBasicDataModel,
                         title: String,
                         isClosedNow: Boolean): Intent {
            val intent = Intent(context, ShopEditScheduleActivity::class.java)
            intent.putExtra(EXTRA_SHOP_MODEL, shopBasicDataModel)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_IS_CLOSED_NOW, isClosedNow)
            return intent
        }
    }

}
