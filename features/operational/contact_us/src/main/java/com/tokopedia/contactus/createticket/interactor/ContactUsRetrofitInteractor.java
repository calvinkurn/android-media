package com.tokopedia.contactus.createticket.interactor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tokopedia.contactus.createticket.model.ContactUsPass;
import com.tokopedia.contactus.createticket.model.solution.SolutionResult;
import com.tokopedia.contactus.inboxticket.model.replyticket.ReplyResult;

import java.util.Map;

/**
 * Created by nisie on 8/12/16.
 */
public interface ContactUsRetrofitInteractor {

    void sendTicket(@NonNull Context context, @NonNull ContactUsPass params,
                    @NonNull SendTicketListener listener);

    void getSolution(@NonNull Context context, @NonNull String id,
                     @NonNull GetSolutionListener listener);

    void unsubscribe();

    void commentRating(Context context, Map<String, String> commentRatingParam, CommentRatingListener listener);

    public interface SendTicketListener {
        void onSuccess();

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    public interface GetSolutionListener{
        void onSuccess(SolutionResult result);

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    interface CommentRatingListener {

        void onSuccess(ReplyResult replyResult);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

        void onFailAuth();

    }

}