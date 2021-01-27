package com.tokopedia.shop.settings.basicinfo.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_IS_CLOSED_NOW
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_MESSAGE
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_SAVE_INSTANCE_CACHE_MANAGER_ID
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_SHOP_BASIC_DATA_MODEL
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopScheduleViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_shop_edit_schedule.*
import java.util.*
import javax.inject.Inject

class ShopEditScheduleActivity : BaseSimpleActivity() {

    companion object {
        private const val SAVED_SELECTED_START_DATE = "svd_selected_start_date"
        private const val SAVED_SELECTED_END_DATE = "svd_selected_end_date"

        @JvmStatic
        fun createIntent(context: Context, draftId: String): Intent {
            return Intent(context, ShopEditScheduleActivity::class.java).apply {
                putExtra(EXTRA_SAVE_INSTANCE_CACHE_MANAGER_ID, draftId)
            }
        }
    }

    @Inject
    lateinit var viewModel: ShopScheduleViewModel

    private var progressDialog: ProgressDialog? = null
    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var snackbar: Snackbar? = null
    private var isClosedNow: Boolean = false
    private var selectedStartCloseUnixTimeMs: Long = 0
    private var selectedEndCloseUnixTimeMs: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE)
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE)
        }

        intent.getStringExtra(EXTRA_SAVE_INSTANCE_CACHE_MANAGER_ID).let {
            val saveInstanceCacheManager = SaveInstanceCacheManager(this, it)
            shopBasicDataModel = saveInstanceCacheManager.get(EXTRA_SHOP_BASIC_DATA_MODEL, ShopBasicDataModel::class.java)
            isClosedNow = saveInstanceCacheManager.get(EXTRA_IS_CLOSED_NOW, Boolean::class.java) ?: false
        }

        DaggerShopSettingsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)

        if (shopBasicDataModel != null) {
            setupView(shopBasicDataModel)
        } else {
            // execute get shop basic data use case
            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
            viewModel.getShopBasicData()
        }

        observeLiveData()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_edit_schedule
    }

    override fun onPause() {
        super.onPause()
        dismissToaster()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SAVED_SELECTED_START_DATE, selectedStartCloseUnixTimeMs)
        outState.putLong(SAVED_SELECTED_END_DATE, selectedEndCloseUnixTimeMs)
    }

    private fun dismissToaster() {
        snackbar?.dismiss()
    }

    private fun setupView(shopBasicDataModel: ShopBasicDataModel?) {
        if (selectedStartCloseUnixTimeMs == 0L || selectedEndCloseUnixTimeMs == 0L) {
            val closeSchedule = shopBasicDataModel?.closeSchedule

            if (isClosedNow) { // if close now, default: H
                selectedStartCloseUnixTimeMs = currentDate.time
                val closedUntil = shopBasicDataModel?.closeUntil
                selectedEndCloseUnixTimeMs = if (StringUtils.isEmptyNumber(closedUntil)) {
                    currentDate.time
                } else {
                    closedUntil!!.toLong() * 1000L
                }
            } else { // if NOT close now, default: H+1
                selectedStartCloseUnixTimeMs = if (StringUtils.isEmptyNumber(closeSchedule)) {
                    tomorrowDate.time
                } else {
                    closeSchedule!!.toLong() * 1000L
                }
                val closedUntil = shopBasicDataModel?.closeUntil
                selectedEndCloseUnixTimeMs = if (StringUtils.isEmptyNumber(closedUntil)) {
                    tomorrowDate.time
                } else {
                    closedUntil!!.toLong() * 1000L
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
            val minDate = unixToDate(selectedStartCloseUnixTimeMs)
            val selectedDate = unixToDate(selectedEndCloseUnixTimeMs)
            showEndDatePickerDialog(selectedDate, minDate)
        }

        etShopCloseNote.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                tilShopCloseNote.error = null
            }
        })
    }

    private fun setupUI() {
        window.decorView.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))
            it.title = getString(R.string.shop_settings_shop_status)
        }

        val tvSave: TextView? = findViewById(R.id.tvSave)
        tvSave?.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            setOnClickListener { onSaveButtonClicked() }
        }
    }

    private fun observeLiveData() {
        observeGetShopBasicData()
        observeUpdateShopSchedule()
    }

    private fun observeGetShopBasicData() {
        observe(viewModel.shopBasicData) { result ->
            result?.let {
                when(it) {
                    is Success -> onSuccessGetShopBasicData(it.data)
                    is Fail -> onFailGetShopBasicData(it.throwable)
                }
            }
        }
    }

    private fun observeUpdateShopSchedule() {
        observe(viewModel.message) { result ->
            result?.let {
                when(it) {
                    is Success -> onSuccessUpdateShopSchedule(it.data)
                    is Fail -> onFailUpdateShopSchedule(it.throwable)
                }
            }
        }
    }

    private fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel
        setupView(shopBasicDataModel)
        hideSubmitLoading()
    }

    private fun onFailGetShopBasicData(throwable: Throwable) {
        hideSubmitLoading()
        showErrorMessage(throwable, View.OnClickListener { viewModel.getShopBasicData() })
    }

    private fun onSuccessUpdateShopSchedule(message: String) {
        hideSubmitLoading()
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_MESSAGE, message) })
        finish()
    }

    private fun onFailUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
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
        hideKeyboard()
        val closeNote = etShopCloseNote.text.toString()
        if (closeNote.isEmpty()) {
            tilShopCloseNote.error = getString(R.string.note_must_be_filled)
            return
        }

        showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
        @ShopScheduleActionDef val shopAction = if (isClosedNow || shopBasicDataModel?.isClosed == true)
            ShopScheduleActionDef.CLOSED
        else
            ShopScheduleActionDef.OPEN
        val closeStart = selectedStartCloseUnixTimeMs
        val closeEnd = selectedEndCloseUnixTimeMs
        viewModel.updateShopSchedule(
                shopAction,
                isClosedNow || shopBasicDataModel?.isClosed == true,
                if (closeStart == 0L) null else closeStart.toString(),
                if (closeEnd == 0L) null else closeEnd.toString(),
                closeNote)
    }

    private fun hideKeyboard() {
        val view = currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
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

    private fun setUIShopSchedule(shopBasicDataModel: ShopBasicDataModel?) {
        //set close schedule
        if (isClosedNow || shopBasicDataModel?.isClosed == true) {
            labelStartClose.isEnabled = false
            setStartCloseDate(currentDate)
        } else {
            labelStartClose.isEnabled = true
            setStartCloseDate(Date(selectedStartCloseUnixTimeMs))
        }

        //set open schedule.
        setEndCloseDate(Date(selectedEndCloseUnixTimeMs))
        etShopCloseNote.setText(shopBasicDataModel?.closeNote)

    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable.cause)
        snackbar = Toaster.build(layout, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            onSaveButtonClicked()
        })
        snackbar?.show()
    }

    private fun showErrorMessage(throwable: Throwable, retryHandler: View.OnClickListener) {
        val message = ErrorHandler.getErrorMessage(this, throwable.cause)
        snackbar = Toaster.build(layout, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), retryHandler)
        snackbar?.show()
    }
}