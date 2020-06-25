package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;

import java.util.List;

/**
 * Created by alvarisi on 3/26/18.
 */

public interface FlightCancellationReasonAndProofContract {
    interface View extends CustomerView{

        List<FlightCancellationAttachmentModel> getAttachments();

        List<FlightCancellationAttachmentModel> getViewAttachments();

        void setAttachment(FlightCancellationAttachmentModel attachment, int position);

        void showRequiredMinimalOneAttachmentErrorMessage(int resId);

        FlightCancellationWrapperModel getCancellationViewModel();

        String getString(int resId);

        void showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(String errorMessage);

        FlightCancellationReasonModel getReason();

        Activity getActivity();

        void showFailedToNextStepErrorMessage(String errorMessage);

        void navigateToNextStep(FlightCancellationWrapperModel viewModel);

        void hideAttachmentContainer();

        void showAttachmentContainer();

        void showProgressBar();

        void hideProgressBar();

        void showAttachmentMinDimensionErrorMessage(int resId);

        void showAttachmentMaxSizeErrorMessage(int resId);

        void addAttachments(List<FlightCancellationAttachmentModel> attachments);

        void deleteAllAttachments();

        void renderAttachment();

        void disableNextButton();

        void enableNextButton();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize(List<FlightCancellationAttachmentModel> attachments);

        List<FlightCancellationAttachmentModel> buildAttachmentList();

        List<FlightCancellationAttachmentModel> buildViewAttachmentList(int docType);

        void setNextButton();

        void onSuccessGetImage(String filepath, int position);

        void onNextButtonClicked();

        void onDestroy();

        void onComeFromEstimateRefundScreen();
    }
}
