package com.tokopedia.nps.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.nps.data.model.FeedbackEntity;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by meta on 28/06/18.
 */
public class FeedbackEntityMapper implements Func1<Response<String>, FeedbackEntity> {

    private Gson gson;

    public FeedbackEntityMapper() {
        this.gson = new Gson();
    }

    @Override
    public FeedbackEntity call(Response<String> response) {
        if (response.isSuccessful()) {
            if (response.body() != null && response.body() != null) {
                try {
                    JSONObject jsonObject  = new JSONObject(response.body());
                    String entity = jsonObject.getString("data");
                    final Type type = new TypeToken<FeedbackEntity>() {}.getType();
                    return gson.fromJson(entity, type);
                } catch (JSONException exeption) {
                    exeption.printStackTrace();
                }
            }
        }
        return null;
    }
}
