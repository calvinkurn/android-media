package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
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
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.EXTRA_SHOP_BASIC_DATA_MODEL
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment.Companion.REQUEST_EDIT_SCHEDULE
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopScheduleViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_edit_schedule.*
import java.util.*
import javax.inject.Inject

class ShopEditScheduleFragment : Fragment() {

    companion object {
        private const val SAVED_SELECTED_START_DATE = "svd_selected_start_date"
        private const val SAVED_SELECTED_END_DATE = "svd_selected_end_date"
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
        initInjector()
        super.onCreate(savedInstanceState)
        setupToolbar()
        if (savedInstanceState != null) {
            selectedStartCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_START_DATE)
            selectedEndCloseUnixTimeMs = savedInstanceState.getLong(SAVED_SELECTED_END_DATE)
        }

        arguments?.let {
            val cacheManagerId = ShopEditBasicInfoFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
            shopBasicDataModel = saveInstanceCacheManager.get(EXTRA_SHOP_BASIC_DATA_MODEL, ShopBasicDataModel::class.java)
            isClosedNow = saveInstanceCacheManager.get(EXTRA_IS_CLOSED_NOW, Boolean::class.java) ?: false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_edit_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (shopBasicDataModel != null) {
            setupView(shopBasicDataModel)
        } else {
            // execute get shop basic data use case
            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
            viewModel.getShopBasicData()
        }

        observeLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
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

    private fun initInjector() {
        (activity?.application as? BaseMainApplication)?.baseAppComponent?.let { baseAppComponent ->
            DaggerShopSettingsComponent.builder()
                    .baseAppComponent(baseAppComponent)
                    .build()
                    .inject(this)
        }
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

    private fun setupToolbar() {
        val toolbar: Toolbar? = activity?.findViewById(R.id.toolbar)
        toolbar?.title = getString(R.string.shop_settings_shop_status)

        val tvSave: TextView? = activity?.findViewById(R.id.tvSave)
        tvSave?.apply {
            visibility = View.VISIBLE
            isEnabled = true
            setTextColor(ContextCompat.getColor(requireContext(), R.color.merchant_green))
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

        val bundle = Bundle().apply {
            putString(EXTRA_MESSAGE, message)
        }
        setNavigationResult(bundle, REQUEST_EDIT_SCHEDULE)
        findNavController().navigateUp()
    }

    private fun onFailUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    private fun showStartDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            setStartCloseDate(toDate(year, month, dayOfMonth))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker.show()
    }

    private fun showEndDatePickerDialog(selectedDate: Date, minDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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
                isClosedNow,
                if (closeStart == 0L) null else closeStart.toString(),
                if (closeEnd == 0L) null else closeEnd.toString(),
                closeNote)
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
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
        val message = ErrorHandler.getErrorMessage(requireContext(), throwable.cause)
        snackbar = Toaster.build(layout, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
            onSaveButtonClicked()
        })
    }

    private fun showErrorMessage(throwable: Throwable, retryHandler: View.OnClickListener) {
        val message = ErrorHandler.getErrorMessage(requireContext(), throwable.cause)
        snackbar = Toaster.build(layout, message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), retryHandler)
    }
}