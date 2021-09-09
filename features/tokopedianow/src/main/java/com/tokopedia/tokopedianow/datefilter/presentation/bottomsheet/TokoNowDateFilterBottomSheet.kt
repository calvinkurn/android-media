import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.DATE_LABEL_POSITION
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.END_DATE
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.START_DATE
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapter
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapterTypeFactory
import com.tokopedia.tokopedianow.datefilter.presentation.differ.DateFilterDiffer
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.tokopedianow.datefilter.presentation.viewholder.DateFilterViewHolder.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottomsheet_tokopedianow_date_filter.*
import java.util.*

class TokoNowDateFilterBottomSheet :
    BottomSheetUnify(),
    DateFilterViewHolderListener {

    companion object {
        private val TAG = TokoNowDateFilterBottomSheet::class.simpleName
        private const val MIN_30_DAYS = -30
        private const val MIN_90_DAYS = -90
        private const val ALL_DATE_TRANSACTION_POSITION = 0
        private const val LAST_ONE_MONTH_POSITION = 1
        private const val LAST_THREE_MONTHS_POSITION = 2
        private const val CUSTOM_DATE_POSITION = 3
        private const val MIN_KEYWORD_CHARACTER_COUNT = 3

        fun newInstance(): TokoNowDateFilterBottomSheet {
            return TokoNowDateFilterBottomSheet()
        }
    }

    private var chosenStartDate: GregorianCalendar? = null
    private var chosenEndDate: GregorianCalendar? = null
    private var rvDate: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null
    private var clChooseDate: ConstraintLayout? = null
    private var tempStartDate: String = ""
    private var tempEndDate: String = ""
    private var dateLabelPosition: Int = 0
    private var listTitles: List<DateFilterUiModel> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        setDefaultDatesForDatePicker()
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
        adapter.submitList(newItemList)

        tempEndDate = startDate
        tempEndDate = endDate
        dateLabelPosition = position

        if (position == CUSTOM_DATE_POSITION) {
            clChooseDate?.show()
        } else {
            clChooseDate?.hide()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager, dateLabelPosition: Int, startDate: String, endDate: String) {
        show(fm, TAG)
        this.dateLabelPosition = dateLabelPosition
        listTitles = listOf(
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_all_date_transactions_bottomsheet,
                isChecked = dateLabelPosition == ALL_DATE_TRANSACTION_POSITION,
                isLastItem = false,
                startDate = "",
                endDate = ""
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_last_one_month_bottomsheet,
                isChecked = dateLabelPosition == LAST_ONE_MONTH_POSITION,
                isLastItem = false,
                startDate = getCalculatedFormattedDate("yyyy-MM-dd", MIN_30_DAYS).toString(),
                endDate = Date().toFormattedString("yyyy-MM-dd")
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_last_three_months_bottomshet,
                isChecked = dateLabelPosition == LAST_THREE_MONTHS_POSITION,
                isLastItem = false,
                startDate = getCalculatedFormattedDate("yyyy-MM-dd", MIN_90_DAYS).toString(),
                endDate = Date().toFormattedString("yyyy-MM-dd")
            ),
            DateFilterUiModel(
                titleRes = R.string.tokopedianow_date_filter_item_custom_date_bottomshet,
                isChecked = dateLabelPosition == CUSTOM_DATE_POSITION,
                isLastItem = true,
                startDate = "",
                endDate = ""
            ),
        )
    }

    private fun setup() {
        tf_start_date?.textFieldInput?.setText(chosenStartDate?.let { it ->
            calendarToStringFormat(
                it, "dd MMM yyyy")
        })
        tf_start_date?.textFieldInput?.isFocusable = false
        tf_start_date?.textFieldInput?.isClickable = true
        tf_start_date?.textFieldInput?.setOnClickListener {
            showDatePicker(START_DATE)
        }

        tf_end_date?.textFieldInput?.setText(chosenEndDate?.let { it ->
            calendarToStringFormat(
                it, "dd MMM yyyy")
        })
        tf_end_date?.textFieldInput?.isFocusable = false
        tf_end_date?.textFieldInput?.isClickable = true
        tf_end_date?.textFieldInput?.setOnClickListener {
            showDatePicker(END_DATE)
        }
    }

    private fun showDatePicker(flag: String) {
        context?.let { context ->
            var minDate = GregorianCalendar()
            var maxDate = GregorianCalendar()
            var currDate = GregorianCalendar()

            if (flag.equals(START_DATE, true)) {
                chosenEndDate?.let { maxDate = it }
                chosenStartDate?.let { currDate = it }
                minDate = getLimitDate()

            } else if (flag.equals(END_DATE, true)) {
                chosenStartDate?.let { minDate = it }
                chosenEndDate?.let { currDate = it }
            }

            val datePicker = DateTimePickerUnify(context, minDate, currDate, maxDate, null, DateTimePickerUnify.TYPE_DATEPICKER).apply {
                datePickerButton.setOnClickListener {
                    val resultDate = getDate()
                    val monthInt = resultDate.get(Calendar.MONTH) + 1
                    var monthStr = monthInt.toString()
                    if (monthStr.length == 1) monthStr = "0$monthStr"

                    var dateStr = resultDate.get(Calendar.DATE).toString()
                    if (dateStr.length == 1) dateStr = "0$dateStr"

                    if (flag.equals(START_DATE, true)) {
                        chosenStartDate = resultDate as GregorianCalendar
                        tf_start_date?.textFieldInput?.setText("${calendarToStringFormat(
                            resultDate as GregorianCalendar, "dd MMM yyyy")}")
                        tempStartDate = calendarToStringFormat(resultDate, "yyyy-MM-dd").toString()

                    } else {
                        chosenEndDate = resultDate as GregorianCalendar
                        tf_end_date?.textFieldInput?.setText("${calendarToStringFormat(
                            resultDate as GregorianCalendar, "dd MMM yyyy")}")
                        tempEndDate = calendarToStringFormat(resultDate, "yyyy-MM-dd").toString()
                    }
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

    private fun calendarToStringFormat(dateParam: GregorianCalendar, format: String) : CharSequence {
        return DateFormat.format(format, dateParam.time)
    }

    private fun getLimitDate(): GregorianCalendar {
        var returnDate = GregorianCalendar()
        val defDate = "2018-09-01"
        val splitDefDate = defDate.split("-")
        if (splitDefDate.isNotEmpty() && splitDefDate.size == MIN_KEYWORD_CHARACTER_COUNT) {
            returnDate = stringToCalendar("${splitDefDate[0].toInt()}-${(splitDefDate[1].toInt()-1)}-${splitDefDate[2].toInt()}")
        }
        return returnDate
    }

    private fun stringToCalendar(stringParam: CharSequence) : GregorianCalendar {
        val split = stringParam.split("-")
        return if (split.isNotEmpty() && split.size == MIN_KEYWORD_CHARACTER_COUNT) {
            GregorianCalendar(split[0].toInt(), split[1].toInt(), split[2].toInt())
        } else GregorianCalendar()
    }

    private fun setDefaultDatesForDatePicker() {
        chosenStartDate = getLimitDate()
        chosenEndDate = GregorianCalendar()
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
        setupItemView(inflater, container)
        setTitle(getString(R.string.tokopedianow_sort_filter_title_bottomsheet))
    }

    private fun setupItemView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokopedianow_date_filter, container)
        rvDate = itemView.findViewById(R.id.rv_date_filter)
        btnApplyFilter = itemView.findViewById(R.id.btn_apply_filter)
        clChooseDate = itemView.findViewById(R.id.cl_choose_date)
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
            val intent = Intent()
            intent.putExtra(DATE_LABEL_POSITION, dateLabelPosition)
            activity?.setResult(Activity.RESULT_OK, intent)
            dismiss()
        }
    }
} 