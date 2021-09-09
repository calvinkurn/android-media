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
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.DATE_LABEL_POSITION
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapter
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterAdapterTypeFactory
import com.tokopedia.tokopedianow.datefilter.presentation.differ.DateFilterDiffer
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.tokopedianow.datefilter.presentation.viewholder.DateFilterViewHolder.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
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

        fun newInstance(): TokoNowDateFilterBottomSheet {
            return TokoNowDateFilterBottomSheet()
        }
    }

    private var rvDate: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null
    private var dateLabelPosition: Int = 0
    private var listTitles: List<DateFilterUiModel> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBtnFilter()
    }

    override fun onClickItem(isChecked: Boolean, position: Int, value: Int) {
        val newItemList = mutableListOf<DateFilterUiModel>()
        listTitles.forEachIndexed { index, model ->
            newItemList.add(model.copy(isChecked = index == position))
        }
        adapter.submitList(newItemList)
        dateLabelPosition = value
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