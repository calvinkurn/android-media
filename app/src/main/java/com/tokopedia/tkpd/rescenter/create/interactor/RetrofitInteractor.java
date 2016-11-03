package com.tokopedia.tkpd.rescenter.create.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.tkpd.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.List;
import java.util.Map;

/**
 * Created on 6/17/16.
 */
public interface RetrofitInteractor {

    void getFormCreateResCenter(@NonNull Context context,
                                @NonNull Map<String, String> params,
                                @NonNull FormResCenterListener listener);

    void getSolution(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull FormSolutionListener listener);

    void unSubscribe();

    interface FormResCenterListener {

        void onSuccess(CreateResCenterFormData form);

        void onTimeout(RetryClickedListener clickedListener);

        void onError(String error, RetryClickedListener clickedListener);

        void onNullData();

    }

    interface FormSolutionListener {

        void onSuccess(List<CreateResCenterFormData.SolutionData> solutionDataList);

        void onTimeout(RetryClickedListener clickedListener);

        void onError(String error);

        void onNullData();
    }
}
