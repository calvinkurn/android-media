package com.tokopedia.otp.cotp.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.otp.cotp.domain.pojo.ListMethodItemPojo;
import com.tokopedia.otp.cotp.domain.pojo.ModeList;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 1/18/18.
 */

public class VerificationMethodMapper implements Func1<Response<DataResponse<ListMethodItemPojo>>,
        ListVerificationMethod> {

    @Inject
    public VerificationMethodMapper() {
    }

    @Override
    public ListVerificationMethod call(Response<DataResponse<ListMethodItemPojo>> dataResponseResponse) {

        ListMethodItemPojo pojo = dataResponseResponse.body().getData();

        int IS_SUCCESS = 1;
        if (pojo.getIsSuccess() == IS_SUCCESS) {
            return convertToDomain(pojo);
        } else {
            throw new RuntimeException("");
        }

    }

    private ListVerificationMethod convertToDomain(ListMethodItemPojo pojo) {
        ArrayList<MethodItem> list = new ArrayList<>();
        for (ModeList modePojo : pojo.getModeList()) {
            list.add(new MethodItem(
                    modePojo.getModeText(),
                    modePojo.getOtpListImgUrl(),
                    modePojo.getOtpListText(),
                    modePojo.getNewAfterOtpListText(),
                    modePojo.isUsingPopUp(),
                    modePojo.getPopUpHeader(),
                    modePojo.getPopUpBody(),
                    modePojo.getNumberOtpDigit()
            ));
        }
        return new ListVerificationMethod(list, pojo.getLinkType());
    }
}
