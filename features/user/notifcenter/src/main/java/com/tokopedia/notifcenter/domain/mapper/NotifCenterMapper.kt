package com.tokopedia.notifcenter.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.notifcenter.domain.pojo.NotifCenterPojo
import com.tokopedia.notifcenter.view.viewmodel.NotifCenterViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by alvinatin on 21/08/18.
 */
class NotifCenterMapper : Func1<Response<DataResponse<NotifCenterPojo>>, NotifCenterViewModel> {
    override fun call(p0: Response<DataResponse<NotifCenterPojo>>?): NotifCenterViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}