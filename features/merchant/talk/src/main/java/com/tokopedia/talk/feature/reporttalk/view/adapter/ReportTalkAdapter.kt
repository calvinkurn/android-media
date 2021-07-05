package com.tokopedia.talk.feature.reporttalk.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.reporttalk.view.uimodel.TalkReportOptionUiModel


/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkAdapter(private val optionClickListener: OnOptionClickListener,
                        var listOption: ArrayList<TalkReportOptionUiModel>) : RecyclerView
.Adapter<ReportTalkAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    interface OnOptionClickListener {
        fun onClickOption(talkReportOptionUiModel: TalkReportOptionUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.report_talk_item))

    override fun getItemCount(): Int {
        return listOption.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.radioButton.isChecked = listOption[position].isChecked
        holder.title.text = listOption[position].reportTitle

        holder.itemView.setOnClickListener {
            optionClickListener.onClickOption(listOption[position])
        }

        holder.radioButton.setOnClickListener {
            optionClickListener.onClickOption(listOption[position])
        }
    }

    fun setChecked(talkReportOptionUiModel: TalkReportOptionUiModel) {
        if (!listOption[talkReportOptionUiModel.position].isChecked) {
            for (talkReportModel in listOption) setUncheck(talkReportModel)
            listOption[talkReportOptionUiModel.position].isChecked = true
            notifyDataSetChanged()
        }
    }

    private fun setUncheck(talkReportModel: TalkReportOptionUiModel) {
        talkReportModel.isChecked = false
    }

    fun getItem(position: Int): TalkReportOptionUiModel {
        return listOption[position]
    }

    fun getSelectedOption(): TalkReportOptionUiModel {
        var selectedOption = TalkReportOptionUiModel()
        for (talkOption in listOption) {
            if (talkOption.isChecked) {
                selectedOption = talkOption
                break
            }
        }
        return selectedOption
    }

}

private fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
