package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class DatePickerSheet {

    private var dialog: BottomSheetDialog? = null
    var onItemClick: ((startDate: Long, endDate: Long, position: Int) -> Unit)? = null
    var customDatepicker: (() -> Unit)? = null

    private fun setupView(context: Context, index: Int, range: String, dateList: ListUnify?, btnClose: ImageView?) {
        dialog?.let { dialog ->
            val listUnify = ArrayList<ListItemUnify>()
            val dateModel = Utils.getPeriodRangeList(context)

            dateModel.forEachIndexed { index, periodRangeModel ->

                val data = if (index != CUSTOM_DATE)
                    ListItemUnify(periodRangeModel.label, periodRangeModel.getDescription(context))
                else
                    ListItemUnify(periodRangeModel.label, range)

                data.isBold = true
                data.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
                listUnify.add(data)
            }
            dateList?.setData(listUnify)

            dateList?.run {
                this.onLoadFinish {
                    this.setOnItemClickListener { parent, view, position, id ->
                        setSelected(listUnify, position) {
                            if (position != CUSTOM_DATE) {
                                onItemClick?.invoke(dateModel[position].startDate, dateModel[position].endDate, position)
                                dialog.dismiss()
                            } else {
                                customDatepicker?.invoke()
                                dialog.dismiss()
                            }
                        }
                    }
                    listUnify.forEachIndexed { position, it ->
                        it.listRightRadiobtn?.setOnClickListener {
                            this.setSelected(listUnify, position) {
                                if (position != CUSTOM_DATE) {
                                    onItemClick?.invoke(dateModel[position].startDate, dateModel[position].endDate, position)
                                    dialog.dismiss()
                                } else {
                                    customDatepicker?.invoke()
                                    dialog.dismiss()
                                }
                            }
                        }
                    }

                    this?.setSelected(listUnify, index) {}
                }
            }
            btnClose?.setOnClickListener {
                dismissDialog()
            }
        }
    }

    private fun ListUnify.setSelected(items: List<ListItemUnify>, position: Int, onChecked: (selectedItem: ListItemUnify) -> Any) = run {
        val selectedItem = this.getItemAtPosition(position) as ListItemUnify
        items.filter { it.getShownRadioButton()?.isChecked ?: false }
                .filterNot { it == selectedItem }
                .onEach { it.getShownRadioButton()?.isChecked = false }
        selectedItem.getShownRadioButton()?.isChecked = true
        onChecked(selectedItem)
    }

    private fun ListItemUnify.getShownRadioButton() = run {
        if (listLeftRadiobtn?.visibility == View.VISIBLE) listLeftRadiobtn
        else if (listRightRadiobtn?.visibility == View.VISIBLE) listRightRadiobtn
        else null
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
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_datepicker_bottomsheet_layout)
            val dateList: ListUnify? = fragment.dialog?.findViewById(R.id.date_list)
            val btnClose: ImageView? = fragment.dialog?.findViewById(R.id.btn_close)
            fragment.setupView(context, index, range, dateList, btnClose)
            return fragment
        }
    }
}
