package com.tokopedia.talk.reporttalk.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.talk.R
import com.tokopedia.talk.reporttalk.view.viewmodel.TalkReportOptionViewModel


/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkAdapter(private val optionClickListener: OnOptionClickListener,
                        var listOption: ArrayList<TalkReportOptionViewModel>) : RecyclerView
.Adapter<ReportTalkAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    interface OnOptionClickListener {
        fun onClickOption(talkReportOptionViewModel: TalkReportOptionViewModel)
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

    fun setChecked(talkReportOptionViewModel: TalkReportOptionViewModel) {
        if (!listOption[talkReportOptionViewModel.position].isChecked) {
            for (talkReportModel in listOption) setUncheck(talkReportModel)
            listOption[talkReportOptionViewModel.position].isChecked = true
            notifyDataSetChanged()
        }
    }

    private fun setUncheck(talkReportModel: TalkReportOptionViewModel) {
        talkReportModel.isChecked = false
    }

    fun getItem(position: Int): TalkReportOptionViewModel {
        return listOption[position]
    }

    fun getSelectedOption(): TalkReportOptionViewModel {
        var selectedOption = TalkReportOptionViewModel()
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
