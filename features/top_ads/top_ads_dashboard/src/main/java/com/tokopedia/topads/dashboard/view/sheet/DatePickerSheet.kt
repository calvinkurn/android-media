package com.tokopedia.topads.dashboard.view.sheet

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.utils.ListUnifyUtils.setSelectedItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class DatePickerSheet {

    private var dialog: BottomSheetDialog? = null
    var onItemClick: ((startDate: Long, endDate: Long, position: Int) -> Unit)? = null
    var customDatepicker: (() -> Unit)? = null
    private val listUnify = ArrayList<ListItemUnify>()

    private fun setupView(context: Context, index: Int, range: String) {

        dialog?.let { dialog ->
            val dateModel = Utils.getPeriodRangeList(context)

            dialog.findViewById<ImageUnify>(R.id.btn_close)
                ?.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_group_close))
            dateModel.forEachIndexed { index, periodRangeModel ->

                val data = if (index != CUSTOM_DATE)
                    ListItemUnify(periodRangeModel.label, periodRangeModel.getDescription(context))
                else
                    ListItemUnify(periodRangeModel.label, range)

                data.isBold = true
                data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
                listUnify.add(data)
            }
            dialog.findViewById<ListUnify>(R.id.date_list)?.setData(listUnify)

            fun onSelect(position: Int) {
                sendAnalytics(position, context)
                if (position != CUSTOM_DATE) {
                    onItemClick?.invoke(dateModel[position].startDate,
                        dateModel[position].endDate,
                        position)
                    dialog.dismiss()
                } else {
                    customDatepicker?.invoke()
                    dialog.dismiss()
                }
            }

            dialog.findViewById<ListUnify>(R.id.date_list)?.run {
                this.onLoadFinish {
                    this.setOnItemClickListener { parent, view, position, id ->
                        this.setSelectedItem(listUnify, position) {
                            onSelect(position)
                        }
                    }
                    listUnify.forEachIndexed { position, it ->
                        it.listRightRadiobtn?.setOnClickListener {
                            this.setSelectedItem(listUnify, position) {
                                onSelect(position)
                            }
                        }
                    }

                    this.setSelectedItem(listUnify, index) {}
                }
            }
            dialog.findViewById<ImageUnify>(R.id.btn_close)?.setOnClickListener {
                dismissDialog()
            }
        }
    }

    private fun sendAnalytics(position: Int, context: Context) {
        val title = listUnify.getOrNull(position)?.listTitle?.text ?: return
        when (title) {
            context.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago) -> {
                TopadsTopupTracker.click7HariTerakhir()
            }
            context.getString(com.tokopedia.datepicker.range.R.string.thirty_days_ago) -> {
                TopadsTopupTracker.click30HariTerakhir()
            }
            context.getString(com.tokopedia.datepicker.range.R.string.label_this_month) -> {
                TopadsTopupTracker.clickBulanIni()
            }
            context.getString(R.string.topads_dash_date_custom) -> {
                TopadsTopupTracker.clickCustom()
            }
            context.getString(com.tokopedia.datepicker.range.R.string.yesterday) -> {
                TopadsTopupTracker.clickKemarin()
            }
            context.getString(com.tokopedia.datepicker.range.R.string.label_today) -> {
                TopadsTopupTracker.clickHariIni()
            }
        }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {

        @SuppressLint("UnifyComponentUsage")
        fun newInstance(context: Context, index: Int, range: String): DatePickerSheet {
            val fragment = DatePickerSheet()
            fragment.dialog = BottomSheetDialog(context,
                com.tokopedia.topads.common.R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_datepicker_bottomsheet_layout)
            fragment.setupView(context, index, range)
            return fragment
        }
    }
}
