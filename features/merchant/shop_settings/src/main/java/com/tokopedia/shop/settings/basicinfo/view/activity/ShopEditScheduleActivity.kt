@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.basicinfo.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.oldview.presenter.UpdateShopSchedulePresenter
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.activity_shop_edit_schedule.*
import kotlinx.android.synthetic.main.partial_toolbar_save_button.*
import java.util.*
import javax.inject.Inject

class ShopEditScheduleActivity : BaseSimpleActivity(), UpdateShopSchedulePresenter.View {

    @Inject
    lateinit var updateShopSchedulePresenter: UpdateShopSchedulePresenter

    private var progressDialog: ProgressDialog? = null

    private var selectedStartCloseUnixTimeMs: Long = 0
    private var selectedEndCloseUnixTimeMs: Long = 0

    private lateinit var shopBasicDataModel: ShopBasicDataModel
    private var isClosedNow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE)
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE)
        }

        DaggerShopSettingsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        updateShopSchedulePresenter.attachView(this)

        title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        isClosedNow = intent.getBooleanExtra(EXTRA_IS_CLOSED_NOW, false)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        if (intent.hasExtra(EXTRA_SHOP_MODEL)) {
            shopBasicDataModel = intent.getParcelableExtra(EXTRA_SHOP_MODEL)
            setupView(shopBasicDataModel)
        } else {
            // execute get shop basic data use case
            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
            updateShopSchedulePresenter.getShopBasicData()
        }
    }

    private fun setupView(shopBasicDataModel: ShopBasicDataModel) {

        if (selectedStartCloseUnixTimeMs == 0L || selectedEndCloseUnixTimeMs == 0L) {
            val closeSchedule = shopBasicDataModel.closeSchedule

            if (isClosedNow) { // if close now, default: H
                selectedStartCloseUnixTimeMs = currentDate.time
                val closedUntil = shopBasicDataModel.closeUntil
                if (StringUtils.isEmptyNumber(closedUntil)) {
                    selectedEndCloseUnixTimeMs = currentDate.time
                } else {
                    selectedEndCloseUnixTimeMs = closedUntil!!.toLong() * 1000L
                }
            } else { // if NOT close now, default: H+1
                if (StringUtils.isEmptyNumber(closeSchedule)) {
                    selectedStartCloseUnixTimeMs = tomorrowDate.time
                } else {
                    selectedStartCloseUnixTimeMs = closeSchedule!!.toLong() * 1000L
                }
                val closedUntil = shopBasicDataModel.closeUntil
                if (StringUtils.isEmptyNumber(closedUntil)) {
                    selectedEndCloseUnixTimeMs = tomorrowDate.time
                } else {
                    selectedEndCloseUnixTimeMs = closedUntil!!.toLong() * 1000L
                }
            }
        }

        setUIShopSchedule(shopBasicDataModel)

        labelStartClose.setOnClickListener {
            val minDate = tomorrowDate
            val selectedDate = unixToDate(selectedStartCloseUnixTimeMs)
            showStartDatePickerDialog(selectedDate, minDate)
        }

        labelEndClose.setOnClickListener {
            val minDate: Date = unixToDate(selectedStartCloseUnixTimeMs)
            val selectedDate: Date = unixToDate(selectedEndCloseUnixTimeMs)
            showEndDatePickerDialog(selectedDate, minDate)
        }

        etShopCloseNote.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                tilShopCloseNote.error = null
            }
        })

        tvSave.visibility = View.VISIBLE
        tvSave.setOnClickListener { onSaveButtonClicked() }
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    private fun showStartDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            setStartCloseDate(toDate(year, month, dayOfMonth))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker.show()
    }

    private fun showEndDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            setEndCloseDate(toDate(year, month, dayOfMonth))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker.show()
    }

    private fun setStartCloseDate(date: Date) {
        selectedStartCloseUnixTimeMs = date.time
        labelStartClose.setContent(toReadableString(FORMAT_DAY_DATE, date))
        // move end date to start date, if the end < start
        if (selectedEndCloseUnixTimeMs > 0 && selectedEndCloseUnixTimeMs < selectedStartCloseUnixTimeMs) {
            setEndCloseDate(Date(selectedStartCloseUnixTimeMs))
        }
    }

    private fun setEndCloseDate(date: Date) {
        selectedEndCloseUnixTimeMs = date.time
        labelEndClose.setContent(toReadableString(FORMAT_DAY_DATE, date))
    }

    private fun onSaveButtonClicked() {
        val closeNote = etShopCloseNote.text.toString()
        if (closeNote.isEmpty()) {
            tilShopCloseNote.error = getString(R.string.note_must_be_filled)
            return
        }

        showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
        @ShopScheduleActionDef val shopAction = if (isClosedNow || shopBasicDataModel.isClosed)
            ShopScheduleActionDef.CLOSED
        else
            ShopScheduleActionDef.OPEN
        val closeStart = selectedStartCloseUnixTimeMs
        val closeEnd = selectedEndCloseUnixTimeMs
        updateShopSchedulePresenter.updateShopSchedule(
                shopAction,
                isClosedNow,
                if (closeStart == 0L) null else closeStart.toString(),
                if (closeEnd == 0L) null else closeEnd.toString(),
                closeNote)
    }

    private fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (progressDialog?.isShowing == false) {
            progressDialog?.setMessage(message)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }
    }

    private fun hideSubmitLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        updateShopSchedulePresenter.detachView()
    }

    override fun onSuccessUpdateShopSchedule(successMessage: String) {
        hideSubmitLoading()
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_MESSAGE, successMessage) })
        finish()
    }

    override fun onErrorUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    override fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel
        setupView(shopBasicDataModel)
        hideSubmitLoading()
    }

    override fun onErrorGetShopBasicData(throwable: Throwable) {
        hideSubmitLoading()
        showErrorMessage(throwable, View.OnClickListener { updateShopSchedulePresenter.getShopBasicData() })
    }

    private fun setUIShopSchedule(shopBasicDataModel: ShopBasicDataModel) {
        //set close schedule
        if (isClosedNow || shopBasicDataModel.isClosed) {
            labelStartClose.isEnabled = false
            setStartCloseDate(currentDate)
        } else {
            labelStartClose.isEnabled = true
            setStartCloseDate(Date(selectedStartCloseUnixTimeMs))
        }

        //set open schedule.
        setEndCloseDate(Date(selectedEndCloseUnixTimeMs))
        etShopCloseNote.setText(shopBasicDataModel.closeNote)

    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable)
        Toaster.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            onSaveButtonClicked()
        })
    }

    private fun showErrorMessage(throwable: Throwable, retryHandler: View.OnClickListener) {
        val message = ErrorHandler.getErrorMessage(this, throwable)
        Toaster.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again), retryHandler)
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

        const val EXTRA_MESSAGE = "message"

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
