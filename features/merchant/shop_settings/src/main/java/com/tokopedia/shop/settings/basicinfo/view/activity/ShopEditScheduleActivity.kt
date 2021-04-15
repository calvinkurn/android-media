package com.tokopedia.shop.settings.basicinfo.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.StringUtils.isEmptyNumber
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

    private var header: HeaderUnify? = null
    private var loader: LoaderUnify? = null
    private var layout: LinearLayout? = null
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
            showLoading()
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
                selectedEndCloseUnixTimeMs = if (isEmptyNumber(closedUntil)) {
                    currentDate.time
                } else {
                    closedUntil!!.toLong() * 1000L
                }
            } else { // if NOT close now, default: H+1
                selectedStartCloseUnixTimeMs = if (isEmptyNumber(closeSchedule)) {
                    tomorrowDate.time
                } else {
                    closeSchedule!!.toLong() * 1000L
                }
                val closedUntil = shopBasicDataModel?.closeUntil
                selectedEndCloseUnixTimeMs = if (isEmptyNumber(closedUntil)) {
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

        tfShopCloseNote.textFieldInput.afterTextChanged {
            tfShopCloseNote.setError(false)
            tfShopCloseNote.setMessage("")
        }
    }

    private fun setupUI() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        loader = findViewById(R.id.loader)
        layout = findViewById(R.id.layout)
        header = findViewById<HeaderUnify>(R.id.header)?.apply {
            setSupportActionBar(this)
            title = getString(R.string.shop_settings_shop_status)
        }

        header?.actionTextView?.apply {
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
        hideLoading()
    }

    private fun onFailGetShopBasicData(throwable: Throwable) {
        hideLoading()
        showErrorMessage(throwable, View.OnClickListener { viewModel.getShopBasicData() })
    }

    private fun onSuccessUpdateShopSchedule(message: String) {
        hideLoading()
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_MESSAGE, message) })
        finish()
    }

    private fun onFailUpdateShopSchedule(throwable: Throwable) {
        hideLoading()
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
        val closeNote = tfShopCloseNote.textFieldInput.text.toString()
        if (closeNote.isEmpty()) {
            tfShopCloseNote.setError(true)
            tfShopCloseNote.setMessage(getString(R.string.note_must_be_filled))
            return
        }

        showLoading()
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

    private fun showLoading() {
        loader?.show()
        layout?.hide()
    }

    private fun hideLoading() {
        loader?.hide()
        layout?.show()
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
        tfShopCloseNote.textFieldInput.setText(shopBasicDataModel?.closeNote)

    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(this, throwable.cause)
        layout?.let {
            snackbar = Toaster.build(it, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again)) {
                onSaveButtonClicked()
            }
            snackbar?.show()
        }
    }

    private fun showErrorMessage(throwable: Throwable, retryHandler: View.OnClickListener) {
        val message = ErrorHandler.getErrorMessage(this, throwable.cause)
        layout?.let {
            snackbar = Toaster.build(it, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), retryHandler)
            snackbar?.show()
        }
    }

}