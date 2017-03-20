package com.tokopedia.core.inboxreputation.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.interactor.ActReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.interactor.ActReputationRetrofitInteractorImpl;
import com.tokopedia.core.inboxreputation.model.actresult.ActResult;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;

public class InboxReviewIntentService extends IntentService
        implements InboxReputationConstant {


    private ActReputationRetrofitInteractor actReputationRetrofitInteractor;

    public InboxReviewIntentService() {
        super(INTENT_NAME);
        actReputationRetrofitInteractor = new ActReputationRetrofitInteractorImpl();
    }

    public static void startActionReview(Context context, Bundle bundle,
                                         ReviewResultReceiver receiver, int type) {
        Intent intent = new Intent(context, InboxReviewIntentService.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(EXTRA_TYPE, 0);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_POST_REPUTATION:
                    handlePostReputation(bundle, receiver);
                    break;
                case ACTION_POST_RESPONSE:
                    handlePostResponse(bundle, receiver);
                    break;
                case ACTION_SKIP_REVIEW:
                    handleSkipReview(bundle, receiver);
                    break;
                case ACTION_DELETE_RESPONSE:
                    handleDeleteResponse(bundle, receiver);
                    break;
                case ACTION_POST_REVIEW:
                    handlePostReview(bundle, receiver);
                    break;
                case ACTION_EDIT_REVIEW:
                    handleEditReview(bundle, receiver);
                    break;
                case ACTION_POST_REPORT:
                    handlePostReport(bundle, receiver);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }

        }
    }

    private void handlePostReport(Bundle bundle, final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_POST_REPORT);

        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, ACTION_POST_REPORT);
        resultData.putParcelable(PARAM_POST_REPORT, param);

        if (param != null) {
            actReputationRetrofitInteractor.postReport(getBaseContext(),
                    param.getReportParam(),
                    new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal melaporkan ulasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleEditReview(Bundle bundle, final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_EDIT_REVIEW);

        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, ACTION_EDIT_REVIEW);
        resultData.putParcelable(PARAM_EDIT_REVIEW, param);

        if (param != null) {
            actReputationRetrofitInteractor.editReview(getApplicationContext(),
                    param, new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal mengubah ulasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));

                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handlePostReview(Bundle bundle, final ResultReceiver receiver) {
        final ActReviewPass param = bundle.getParcelable(PARAM_POST_REVIEW);

        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, ACTION_POST_REVIEW);
        resultData.putParcelable(PARAM_POST_REVIEW, param);

        if (param != null) {
            actReputationRetrofitInteractor.postReview(getApplicationContext(),
                    param, new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal memberikan ulasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));

                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleDeleteResponse(Bundle bundle, final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_DELETE_RESPONSE);
        int productPosition = bundle.getInt(EXTRA_PRODUCT_POSITION);

        if (param != null) {

            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_DELETE_RESPONSE);
            resultData.putString(EXTRA_REPUTATION_ID, param.getReputationId());
            resultData.putInt(EXTRA_PRODUCT_POSITION, productPosition);
            resultData.putParcelable(PARAM_DELETE_RESPONSE, param);

            actReputationRetrofitInteractor.deleteComment(getApplicationContext(),
                    param.getDeleteParam(),
                    new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal menghapus balasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleSkipReview(Bundle bundle, final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_SKIP_REVIEW);
        int productPosition = bundle.getInt(EXTRA_PRODUCT_POSITION);

        if (param != null) {

            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_SKIP_REVIEW);
            resultData.putString(EXTRA_REPUTATION_ID, param.getReputationId());
            resultData.putInt(EXTRA_PRODUCT_POSITION, productPosition);
            resultData.putParcelable(PARAM_SKIP_REVIEW, param);

            actReputationRetrofitInteractor.skipReview(getApplicationContext(),
                    param.getSkipParam(),
                    new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal melewati ulasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            resultData.putString(EXTRA_ERROR, e.toString());
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handlePostResponse(Bundle bundle, final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_POST_RESPONSE);

        if (param != null) {

            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_POST_RESPONSE);
            resultData.putParcelable(PARAM_POST_RESPONSE, param);

            actReputationRetrofitInteractor.postComment(getApplicationContext(),
                    param.getReplyParam(),
                    new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal membalas ulasan");
                                receiver.send(STATUS_ERROR, resultData);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            resultData.putString(EXTRA_ERROR, e.toString());
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handlePostReputation(@NonNull final Bundle bundle,
                                      final ResultReceiver receiver) {
        ActReviewPass param = bundle.getParcelable(PARAM_POST_REPUTATION);

        if (param != null) {

            final Bundle resultData = new Bundle();
            resultData.putInt(EXTRA_TYPE, ACTION_POST_REPUTATION);
            resultData.putString(EXTRA_SMILEY, param.getReputationScore());
            resultData.putString(EXTRA_REPUTATION_ID, param.getReputationId());
            resultData.putParcelable(PARAM_POST_REPUTATION, param);

            actReputationRetrofitInteractor.postReputation(getApplicationContext(),
                    param.getInsertReputationParam(),
                    new ActReputationRetrofitInteractor.ActReputationListener() {
                        @Override
                        public void onSuccess(ActResult result) {
                            if (result.getIsSuccess() == 1) {
                                resultData.putParcelable(EXTRA_RESULT, result);
                                receiver.send(STATUS_SUCCESS, resultData);
                            } else {
                                resultData.putString(EXTRA_ERROR, "Gagal mengirim reputasi");
                                receiver.send(STATUS_ERROR, resultData);

                            }

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_network_error));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onFailAuth() {
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            resultData.putString(EXTRA_ERROR, e.toString());
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onNullData() {
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }

    }
}
