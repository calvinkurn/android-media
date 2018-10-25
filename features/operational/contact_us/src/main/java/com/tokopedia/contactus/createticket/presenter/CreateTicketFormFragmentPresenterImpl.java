package com.tokopedia.contactus.createticket.presenter;

import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.createticket.fragment.CreateTicketFormFragment;
import com.tokopedia.contactus.createticket.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.contactus.createticket.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.contactus.createticket.listener.CreateTicketFormFragmentView;
import com.tokopedia.contactus.createticket.model.ContactUsPass;
import com.tokopedia.contactus.createticket.model.solution.SolutionResult;

/**
 * Created by nisie on 8/12/16.
 */
public class CreateTicketFormFragmentPresenterImpl implements CreateTicketFormFragmentPresenter,
        ContactUsConstant {

    CreateTicketFormFragmentView viewListener;
    ContactUsRetrofitInteractor networkInteractor;
    CreateTicketFormFragment.FinishContactUsListener listener;

    public CreateTicketFormFragmentPresenterImpl(CreateTicketFormFragmentView viewListener, CreateTicketFormFragment.FinishContactUsListener listener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ContactUsRetrofitInteractorImpl();
        this.listener = listener;
    }

    @Override
    public void sendTicket() {
        if (isTicketValid()) {
            viewListener.showLoading();
            networkInteractor.sendTicket(viewListener.getActivity(), getSendTicketParam(), new ContactUsRetrofitInteractor.SendTicketListener() {
                @Override
                public void onSuccess() {
                    viewListener.finishLoading();
                    if (listener != null) {
                        listener.onFinishCreateTicket();
                    }
                }

                @Override
                public void onNoNetworkConnection() {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onTimeout(String error) {
                    viewListener.finishLoading();
                    viewListener.showError("");

                }

                @Override
                public void onError(String s) {
                    viewListener.finishLoading();
                    viewListener.showError(s);

                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.showError(viewListener.getString(R.string.default_request_error_null_data));

                }
            });
        }
    }

    private ContactUsPass getSendTicketParam() {
        ContactUsPass pass = new ContactUsPass();
        pass.setSolutionId(String.valueOf(viewListener.getArguments().getString(
                ContactUsActivity.PARAM_SOLUTION_ID)));
        pass.setMessageBody(viewListener.getMessage().getText().toString());
        if (!viewListener.getAttachment().isEmpty())
            pass.setAttachment(viewListener.getAttachment());
        pass.setName(SessionHandler.getLoginName(viewListener.getActivity()));
        pass.setTag(String.valueOf(viewListener.getArguments().getString(
                ContactUsActivity.PARAM_TAG)));
        if (viewListener.getPhoneNumber().trim().length() > 0)
            pass.setPhoneNumber(String.valueOf(viewListener.getPhoneNumber()));
        if (viewListener.getArguments().getString(
                ContactUsActivity.PARAM_ORDER_ID, "").length() > 0)
            pass.setOrderId(String.valueOf(viewListener.getArguments().getString(
                    ContactUsActivity.PARAM_ORDER_ID)));
        if (viewListener.getArguments().getString(
                ContactUsActivity.PARAM_INVOICE_ID, "").length() > 0)
            pass.setInvoiceNumber(String.valueOf(viewListener.getArguments().getString(
                    ContactUsActivity.PARAM_INVOICE_ID)));
        if (!SessionHandler.isV4Login(viewListener.getActivity())) {
            pass.setName(viewListener.getName().getText().toString());
            pass.setEmail(viewListener.getEmail().getText().toString());
        }
        return pass;
    }

    private boolean isTicketValid() {

        if (!SessionHandler.isV4Login(viewListener.getActivity())) {
            if (viewListener.getName().getText().toString().trim().length() == 0) {
                viewListener.showErrorValidation(viewListener.getName(), viewListener.getString(R.string.error_field_required));
                return false;
            }

            if (viewListener.getEmail().getText().toString().trim().length() == 0) {
                viewListener.showErrorValidation(viewListener.getEmail(), viewListener.getString(R.string.error_field_required));
                return false;
            } else if (!CommonUtils.EmailValidation(viewListener.getEmail().getText().toString())) {
                viewListener.showErrorValidation(viewListener.getEmail(), viewListener.getString(R.string.error_invalid_email));
                return false;
            }
        }

        if (viewListener.getMessage().getText().toString().trim().length() == 0) {
            viewListener.showErrorValidation(viewListener.getMessage(), viewListener.getString(R.string.error_detail_empty));
            return false;
        } else if (viewListener.getMessage().getText().toString().trim().length() < 30) {
            viewListener.showErrorValidation(viewListener.getMessage(), viewListener.getString(R.string.error_detail_too_short));
            return false;
        } else if (viewListener.getAttachmentNote().getVisibility() == View.VISIBLE && viewListener.getAttachment().isEmpty()) {
            viewListener.showError(viewListener.getActivity().getString(R.string.error_attachment));
            return false;
        }


        return true;
    }

    @Override
    public void initForm() {
        viewListener.showLoading();
        viewListener.removeErrorEmptyState();
        networkInteractor.getSolution(viewListener.getActivity(),
                viewListener.getArguments().getString(ContactUsActivity.PARAM_SOLUTION_ID, "0"),
                new ContactUsRetrofitInteractor.GetSolutionListener() {
                    @Override
                    public void onSuccess(SolutionResult result) {
                        viewListener.setResult(result);

                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        viewListener.showErrorEmptyState("", new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                initForm();
                            }
                        });
                    }

                    @Override
                    public void onTimeout(String error) {
                        viewListener.finishLoading();
                        viewListener.showErrorEmptyState(error, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                initForm();
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {
                        viewListener.finishLoading();
                        viewListener.showErrorEmptyState(s, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                initForm();
                            }
                        });
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showErrorEmptyState(viewListener.getString(R.string.default_request_error_null_data),
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        initForm();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }
}