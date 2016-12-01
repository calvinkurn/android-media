package com.tokopedia.core.inboxmessage.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.inboxmessage.InboxMessageConstant;
import com.tokopedia.core.inboxmessage.interactor.InboxMessageActRetrofitInteractor;
import com.tokopedia.core.inboxmessage.interactor.InboxMessageActRetrofitInteractorImpl;
import com.tokopedia.core.inboxmessage.model.ActInboxMessagePass;
import com.tokopedia.core.inboxmessage.model.inboxmessagedetail.InboxMessageDetail;


public class InboxMessageIntentService extends IntentService implements InboxMessageConstant {

    InboxMessageActRetrofitInteractor actNetworkInteractor;


    public InboxMessageIntentService() {
        super(INTENT_NAME);
        this.actNetworkInteractor = new InboxMessageActRetrofitInteractorImpl();
    }

    public static void startAction(Context context, Bundle bundle,
                                   InboxMessageResultReceiver receiver, int type) {
        Intent intent = new Intent(context, InboxMessageIntentService.class);
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
                case ACTION_ARCHIVE_MESSAGE:
                    handleArchiveMessage(bundle, receiver, action);
                    break;
                case ACTION_UNDO_ARCHIVE_MESSAGE:
                    handleUndoArchiveMessage(bundle, receiver, action);
                    break;
                case ACTION_MOVE_TO_INBOX:
                    handleMoveToInbox(bundle, receiver, action);
                    break;
                case ACTION_UNDO_MOVE_TO_INBOX:
                    handleUndoMoveToInbox(bundle, receiver, action);
                    break;
                case ACTION_DELETE_MESSAGE:
                    handleDeleteMessage(bundle, receiver, action);
                    break;
                case ACTION_UNDO_DELETE_MESSAGE:
                    handleUndoDeleteMessage(bundle, receiver, action);
                    break;
                case ACTION_DELETE_FOREVER:
                    handleDeleteForever(bundle, receiver, action);
                    break;
                case ACTION_SEND_REPLY:
                    handleSendReply(bundle, receiver, action);
                    break;
                case ACTION_FLAG_SPAM:
                    handleFlagSpam(bundle, receiver, action);
                    break;
                case ACTION_UNDO_FLAG_SPAM:
                    handleUndoFlagSpam(bundle, receiver, action);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }

        }
        else{
            CommonUtils.dumper("Failed onHandle Intent");
        }
    }

    private void handleArchiveMessage(Bundle bundle, final ResultReceiver receiver, int action) {

        ActInboxMessagePass param = bundle.getParcelable(PARAM_ARCHIVE_MESSAGE);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);

        if (param != null) {
            actNetworkInteractor.archiveMessage(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }


    private void handleUndoArchiveMessage(Bundle bundle, final ResultReceiver receiver, int action) {

        ActInboxMessagePass param = bundle.getParcelable(PARAM_UNDO_ARCHIVE_MESSAGE);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.undoArchiveMessage(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleMoveToInbox(Bundle bundle, final ResultReceiver receiver, int action) {

        ActInboxMessagePass param = bundle.getParcelable(PARAM_MOVE_TO_INBOX);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.moveToInbox(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleUndoMoveToInbox(Bundle bundle, final ResultReceiver receiver, int action) {
        ActInboxMessagePass param = bundle.getParcelable(PARAM_UNDO_MOVE_TO_INBOX);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.undoMoveToInbox(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }

    }

    private void handleDeleteMessage(Bundle bundle, final ResultReceiver receiver, int action) {
        ActInboxMessagePass param = bundle.getParcelable(PARAM_DELETE_MESSAGE);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.deleteMessage(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleUndoDeleteMessage(Bundle bundle, final ResultReceiver receiver, int action) {
        ActInboxMessagePass param = bundle.getParcelable(PARAM_UNDO_DELETE_MESSAGE);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.undoDeleteMessage(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleDeleteForever(Bundle bundle, final ResultReceiver receiver, int action) {
        ActInboxMessagePass param = bundle.getParcelable(PARAM_DELETE_FOREVER);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.deleteForever(getBaseContext(), param.getMoveInboxParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }

    private void handleSendReply(Bundle bundle, final ResultReceiver receiver, int action) {

        ActInboxMessagePass param = bundle.getParcelable(PARAM_SEND_REPLY);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);


        if (param != null) {
            actNetworkInteractor.replyMessage(
                    getBaseContext(),
                    param.getSendReplyParam(),
                    new InboxMessageActRetrofitInteractor.SendReplyInboxMessageListener() {
                        @Override
                        public void onSuccess(InboxMessageDetail result) {
                            resultData.putParcelable(EXTRA_RESULT, result);
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);

                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }

    }

    private void handleFlagSpam(Bundle bundle, final ResultReceiver receiver, int action) {

        ActInboxMessagePass param = bundle.getParcelable(PARAM_FLAG_SPAM);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);

        if (param != null) {
            actNetworkInteractor.flagDetailSpam(getBaseContext(),
                    param.getFlagSpamParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);

                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }

    }

    private void handleUndoFlagSpam(Bundle bundle, final ResultReceiver receiver, int action) {
        ActInboxMessagePass param = bundle.getParcelable(PARAM_UNDO_FLAG_SPAM);
        final Bundle resultData = new Bundle();
        resultData.putInt(EXTRA_TYPE, action);
        resultData.putAll(bundle);

        if (param != null) {
            actNetworkInteractor.undoFlagDetailSpam(getBaseContext(),
                    param.getFlagSpamParam(),
                    new InboxMessageActRetrofitInteractor.ActInboxMessageListener() {
                        @Override
                        public void onSuccess() {
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {

                        }

                        @Override
                        public void onNoInternetConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }
                    });
        }
    }
}
