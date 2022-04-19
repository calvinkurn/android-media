package com.tokopedia.flight.cancellation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentButtonModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentAdapter(adapterTypeFactory: FlightCancellationAttachmentTypeFactory)
    : BaseAdapter<FlightCancellationAttachmentTypeFactory>(adapterTypeFactory) {

    fun getData(): MutableList<FlightCancellationAttachmentModel> {
        val data = arrayListOf<FlightCancellationAttachmentModel>()
        for (item in visitables) {
            try {
                data.add(item as FlightCancellationAttachmentModel)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        return data
    }

    fun removeAttachment(attachment: FlightCancellationAttachmentModel) {
        val data = getData()
        val index = data.indexOf(attachment)
        if (index != -1) {
            data.removeAt(index)
            visitables.clear()
            visitables.addAll(data)
            showAttachmentButton()
        }
    }

    private fun showAttachmentButton() {
        visitables.add(FlightCancellationAttachmentButtonModel())
        notifyDataSetChanged()
    }

}