package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import java.util.List;

/**
 * Created by alvarisi on 3/26/18.
 */

public interface FlightCancellationReasonAndProofContract {
    interface View extends CustomerView{

        List<FlightCancellationAttachmentViewModel> getAttachments();

        void setAttachment(FlightCancellationAttachmentViewModel attachment, int position);

        void showRequiredMinimalOneAttachmentErrorMessage(int resId);

        FlightCancellationWrapperViewModel getCancellationViewModel();

        String getString(int resId);

        void showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(String errorMessage);

        FlightCancellationReasonViewModel getReason();

        Activity getActivity();

        void showFailedToNextStepErrorMessage(String errorMessage);

        void navigateToNextStep(FlightCancellationWrapperViewModel viewModel);

        void hideAttachmentContainer();

        void showAttachmentContainer();

        void showProgressBar();

        void hideProgressBar();

        void showAttachmentMinDimensionErrorMessage(int resId);

        void showAttachmentMaxSizeErrorMessage(int resId);

        void addAttachments(List<FlightCancellationAttachmentViewModel> attachments);

        void deleteAllAttachments();

        void renderAttachment();

        void setUploadingPosition(int position);

        int getUploadingPosition();

        void updateUploadingProgress(long percentage);

        void disableNextButton();

        void enableNextButton();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize(List<FlightCancellationAttachmentViewModel> attachments);

        List<FlightCancellationAttachmentViewModel> buildAttachmentList();

        void setNextButton();

        void onSuccessGetImage(String filepath, int position);

        void onNextButtonClicked();

        void onDestroy();

        void onComeFromEstimateRefundScreen();
    }
}
