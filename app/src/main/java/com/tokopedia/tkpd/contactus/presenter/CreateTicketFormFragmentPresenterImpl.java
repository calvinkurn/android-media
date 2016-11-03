package com.tokopedia.tkpd.contactus.presenter;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.contactus.ContactUsConstant;
import com.tokopedia.tkpd.contactus.activity.ContactUsActivity;
import com.tokopedia.tkpd.contactus.fragment.CreateTicketFormFragment;
import com.tokopedia.tkpd.contactus.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.tkpd.contactus.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.tkpd.contactus.listener.CreateTicketFormFragmentView;
import com.tokopedia.tkpd.contactus.model.ContactUsPass;
import com.tokopedia.tkpd.contactus.model.contactusform.TicketForm;
import com.tokopedia.tkpd.network.NetworkErrorHelper;

import java.util.HashMap;

/**
 * Created by nisie on 8/12/16.
 */
public class CreateTicketFormFragmentPresenterImpl implements CreateTicketFormFragmentPresenter,
        ContactUsConstant{

    CreateTicketFormFragmentView viewListener;
    ContactUsRetrofitInteractor networkInteractor;
    CreateTicketFormFragment.FinishContactUsListener listener;

    public CreateTicketFormFragmentPresenterImpl(CreateTicketFormFragmentView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ContactUsRetrofitInteractorImpl();
        this.listener = (ContactUsActivity) viewListener.getActivity();
    }

    @Override
    public void sendTicket() {
        if(isTicketValid()){
            viewListener.showLoading();
            networkInteractor.sendTicket(viewListener.getActivity(), getSendTicketParam(), new ContactUsRetrofitInteractor.SendTicketListener() {
                @Override
                public void onSuccess() {
                    viewListener.finishLoading();
                    listener.onFinishCreateTicket();
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
        pass.setTicketCategoryId(String.valueOf(viewListener.getArguments().getInt(PARAM_LAST_CATEGORY_ID)));
        pass.setMessageBody(viewListener.getMessage());
        pass.setAttachment(viewListener.getAttachment());
        return pass;
    }

    private boolean isTicketValid() {

        if (viewListener.getMessage().trim().length() == 0) {
            viewListener.showError(viewListener.getString(R.string.error_detail_empty));
            return false;
        } else if (viewListener.getMessage().trim().length() < 30) {
            viewListener.showError(viewListener.getString(R.string.error_detail_too_short));
            return false;
        }
        return true;
    }

    @Override
    public void initForm() {
        viewListener.showLoading();
        viewListener.removeErrorEmptyState();
        networkInteractor.getFormModelContactUs(viewListener.getActivity(), getFormParam(), new ContactUsRetrofitInteractor.GetContactFormListener() {

            @Override
            public void onSuccess(TicketForm ticketForm) {
                viewListener.setResult(ticketForm.getList().get(0));
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

    private HashMap<String, String> getFormParam() {
        ContactUsPass pass = new ContactUsPass();
        pass.setTicketCategoryId(viewListener.getTicketCategoryId());
        return pass.getContactUsFormParam();
    }
}
