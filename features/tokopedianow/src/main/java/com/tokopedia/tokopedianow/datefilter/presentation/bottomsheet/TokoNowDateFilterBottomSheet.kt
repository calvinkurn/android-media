import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.DateUtil.calendarToStringFormat
import com.tokopedia.tokopedianow.common.util.DateUtil.getGregorianCalendar
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.EXTRA_SELECTED_DATE_FILTER
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapter
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapterTypeFactory
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.differ.DateFilterDiffer
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.tokopedianow.datefilter.presentation.viewholder.DateFilterViewHolder.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottomsheet_tokopedianow_date_filter.*
import java.util.*

class TokoNowDateFilterBottomSheet :
    BottomSheetUnify(),
    DateFilterViewHolderListener {

    companion object {
        private val TAG = TokoNowDateFilterBottomSheet::class.simpleName
        const val ALL_DATE_TRANSACTION_POSITION = 0
        const val LAST_ONE_MONTH_POSITION = 1
        const val LAST_THREE_MONTHS_POSITION = 2
        const val CUSTOM_DATE_POSITION = 3
        private const val MIN_30_DAYS = -30
        private const val MIN_90_DAYS = -90
        private const val START_DATE = "start_date"
        private const val END_DATE = "end_date"

        fun newInstance(): TokoNowDateFilterBottomSheet {
            return TokoNowDateFilterBottomSheet()
        }
    }

    private var chosenStartDate: GregorianCalendar? = null
    private var chosenEndDate: GregorianCalendar? = null
    private var rvDate: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null
    private var tempStartDate: String = ""
    private var tempEndDate: String = ""
    private var tempPosition: Int = 0
    private var selectedFilter: RepurchaseSortFilterUiModel.SelectedDateFilter? = null
    private var listTitles: MutableList<DateFilterUiModel> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBtnFilter()
    }

    override fun onClickItem(isChecked: Boolean, position: Int, startDate: String, endDate: String) {
        val newItemList = mutableListOf<DateFilterUiModel>()
        listTitles.forEachIndexed { index, model ->
            newItemList.add(model.copy(isChecked = index == position))
        }
        listTitles.clear()
        listTitles.addAll(newItemList)
        adapter.submitList(listTitles)

        tempStartDate = startDate
        tempEndDate = endDate
        tempPosition = position
    }

    override fun onOpenBottomSheet(flag: String) {
        showDatePicker(flag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager, selectedFilter: RepurchaseSortFilterUiModel.SelectedDateFilter?) {
        show(fm, TAG)

        this.selectedFilter = selectedFilter
        if (selectedFilter?.position == CUSTOM_DATE_POSITION) {
            chosenStartDate = getGregorianCalendar(selectedFilter.startDate)
            chosenEndDate = getGregorianCalendar(selectedFilter.endDate)
        } else {
            chosenStartDate = getGregorianCalendar("2018-09-01")
            chosenEndDate = GregorianCalendar()
        }

        tempStartDate = convertCalendarToStringWithFormat(chosenStartDate)
        tempEndDate = convertCalendarToStringWithFormat(chosenEndDate)

        listTitles.clear()
        listTitles.addAll(listOf(
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_all_date_transactions_bottomsheet,
                isChecked = selectedFilter?.position == ALL_DATE_TRANSACTION_POSITION,
                isLastItem = false,
                startDate = "",
                endDate = ""
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_last_one_month_bottomsheet,
                isChecked = selectedFilter?.position == LAST_ONE_MONTH_POSITION,
                isLastItem = false,
                startDate = getCalculatedFormattedDate("yyyy-MM-dd", MIN_30_DAYS).toString(),
                endDate = Date().toFormattedString("yyyy-MM-dd")
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_last_three_months_bottomshet,
                isChecked = selectedFilter?.position == LAST_THREE_MONTHS_POSITION,
                isLastItem = false,
                startDate = getCalculatedFormattedDate("yyyy-MM-dd", MIN_90_DAYS).toString(),
                endDate = Date().toFormattedString("yyyy-MM-dd")
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_custom_date_bottomshet,
                isChecked = selectedFilter?.position == CUSTOM_DATE_POSITION,
                isLastItem = true,
                startDate = tempStartDate,
                endDate = tempEndDate
            )
        ))
    }

    private fun convertCalendarToStringWithFormat(date: GregorianCalendar?): String {
        return date?.let { it -> calendarToStringFormat(it, "yyyy-MM-dd") }.toString()
    }

    private fun showDatePicker(flag: String) {
        context?.let { context ->
            var minDate = GregorianCalendar()
            var maxDate = GregorianCalendar()
            var currDate = GregorianCalendar()

            if (flag.equals(START_DATE, true)) {
                chosenEndDate?.let { maxDate = it }
                chosenStartDate?.let { currDate = it }
                minDate = getGregorianCalendar("2018-09-01")

            } else if (flag.equals(END_DATE, true)) {
                chosenStartDate?.let { minDate = it }
                chosenEndDate?.let { currDate = it }
            }

            val datePicker = DateTimePickerUnify(context, minDate, currDate, maxDate, null, DateTimePickerUnify.TYPE_DATEPICKER).apply {
                datePickerButton.setOnClickListener {
                    val resultDate = getDate()
                    tempPosition = CUSTOM_DATE_POSITION

                    val item = if (flag.equals(START_DATE, true)) {
                        chosenStartDate = resultDate as GregorianCalendar
                        tempStartDate = convertCalendarToStringWithFormat(chosenStartDate)
                        listTitles[CUSTOM_DATE_POSITION].copy(startDate = tempStartDate)
                    } else {
                        chosenEndDate = resultDate as GregorianCalendar
                        tempEndDate = convertCalendarToStringWithFormat(chosenEndDate)
                        listTitles[CUSTOM_DATE_POSITION].copy(endDate = tempEndDate)
                    }

                    val newItemList = mutableListOf<DateFilterUiModel>()
                    newItemList.addAll(listTitles)
                    newItemList[CUSTOM_DATE_POSITION] = item

                    listTitles.clear()
                    listTitles.addAll(newItemList)
                    adapter.submitList(listTitles)

                    dismiss()
                }

                if (flag.equals(START_DATE, true)) {
                    setTitle(context.getString(R.string.tokopedianow_date_filter_item_start_date_bottomshet))
                } else {
                    setTitle(context.getString(R.string.tokopedianow_date_filter_item_end_date_bottomshet))
                }
                setCloseClickListener { dismiss() }
            }
            datePicker.show(parentFragmentManager, "")
        }
    }

    private val adapter by lazy {
        DateFilterAdapter(
            DateFilterAdapterTypeFactory(this),
            DateFilterDiffer()
        )
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        selectedFilter = RepurchaseSortFilterUiModel.SelectedDateFilter()
        setupItemView(inflater, container)
        setTitle(getString(R.string.tokopedianow_sort_filter_title_bottomsheet))
    }

    private fun setupItemView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokopedianow_date_filter, container)
        rvDate = itemView.findViewById(R.id.rv_date_filter)
        btnApplyFilter = itemView.findViewById(R.id.btn_apply_filter)
        setChild(itemView)
    }

    private fun setupRecyclerView() {
        rvDate?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TokoNowDateFilterBottomSheet.adapter
            itemAnimator = null
        }
        adapter.submitList(listTitles)
    }

    private fun setupBtnFilter() {
        btnApplyFilter?.setOnClickListener {
            selectedFilter?.apply {
                startDate = tempStartDate
                endDate = tempEndDate
                position = tempPosition
            }

            val intent = Intent()
            intent.putExtra(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
            activity?.setResult(Activity.RESULT_OK, intent)
            dismiss()
        }
    }
} 