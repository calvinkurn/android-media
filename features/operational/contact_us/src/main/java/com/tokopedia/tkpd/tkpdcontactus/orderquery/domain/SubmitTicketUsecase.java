package com.tokopedia.tkpd.tkpdcontactus.orderquery.domain;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.QueryTicket;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.SubmitTicketResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketUsecase extends UseCase<SubmitTicketResponse>{
    ISubmitTicketRepository repository;


    public SubmitTicketUsecase(ISubmitTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SubmitTicketResponse> createObservable(RequestParams requestParams) {
        // TODO Need to   update
        return repository.submitTickets(null);
    }

    protected HashMap<String, RequestBody> generateRequestBody(RequestParams requestParams) {

        /*RequestBody isAudio = RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(IS_AUDIO,
                        "false"));
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put(IS_AUDIO, isAudio);*/
        return null;
    }

    private MultipartBody.Part generateRequestAudio(RequestParams requestParams) {

        /*File file = new File(requestParams.getString(AUDIO_PATH, ""));
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/wav"), file);

        return MultipartBody.Part.createFormData("tkp_file", file.getName(), requestBody);*/
        return null;

    }

}
