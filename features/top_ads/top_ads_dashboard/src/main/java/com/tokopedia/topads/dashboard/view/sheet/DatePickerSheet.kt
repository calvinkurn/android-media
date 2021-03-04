package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.utils.ListUnifyUtils.setSelectedItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.topads_dash_datepicker_bottomsheet_layout.*

class DatePickerSheet {

    private var dialog: BottomSheetDialog? = null
    var onItemClick: ((startDate: Long, endDate: Long, position: Int) -> Unit)? = null
    var customDatepicker: (() -> Unit)? = null

    private fun setupView(context: Context, index: Int, range: String) {

        dialog?.let { dialog ->
            val listUnify = ArrayList<ListItemUnify>()
            val dateModel = Utils.getPeriodRangeList(context)

            dialog.btn_close.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_group_close))
            dateModel.forEachIndexed { index, periodRangeModel ->

                val data = if (index != CUSTOM_DATE)
                    ListItemUnify(periodRangeModel.label, periodRangeModel.getDescription(context))
                else
                    ListItemUnify(periodRangeModel.label, range)

                data.isBold = true
                data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
                listUnify.add(data)
            }
            dialog.date_list.setData(listUnify)

            fun onSelect(position: Int) {
                if (position != CUSTOM_DATE) {
                    onItemClick?.invoke(dateModel[position].startDate, dateModel[position].endDate, position)
                    dialog.dismiss()
                } else {
                    customDatepicker?.invoke()
                    dialog.dismiss()
                }
            }

            dialog.date_list.run {
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
            dialog.btn_close.setOnClickListener {
                dismissDialog()
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

        fun newInstance(context: Context, index: Int, range: String): DatePickerSheet {
            val fragment = DatePickerSheet()
            fragment.dialog = BottomSheetDialog(context, com.tokopedia.topads.common.R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_datepicker_bottomsheet_layout)
            fragment.setupView(context, index, range)
            return fragment
        }
    }
}
