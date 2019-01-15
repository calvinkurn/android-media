//package com.tokopedia.topchat.chatroom.data.mapper;
//
//import android.text.TextUtils;
//
//import com.tokopedia.core.app.MainApplication;
//import com.tokopedia.core.network.ErrorMessageException;
//import com.tokopedia.core.network.retrofit.response.ErrorHandler;
//import com.tokopedia.core.network.retrofit.response.TkpdResponse;
//import com.tokopedia.topchat.R;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//import retrofit2.Response;
//import rx.functions.Func1;
//
///**
// * Created by Hendri on 07/06/18.
// */
//public abstract class BaseChatApiCallMapper<I, O> implements Func1<Response<TkpdResponse>, O>{
//    /**
//     * Returns a {@link Type} object to identify generic types
//     * @return type
//     */
//    private Type getGenericClassType(int index){
//        Type type = getClass().getGenericSuperclass();
//        while (!(type instanceof ParameterizedType)) {
//            if (type instanceof ParameterizedType) {
//                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
//            } else {
//                type = ((Class<?>) type).getGenericSuperclass();
//            }
//        }
//        return ((ParameterizedType) type).getActualTypeArguments()[index];
//    }
//
//    @Override
//    public O call(Response<TkpdResponse> response) {
//        if (response.isSuccessful()) {
//            if ((!response.body().isNullData()
//                    && response.body().getErrorMessageJoined().equals(""))
//                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
//                I data = response.body().convertDataObj((Class<I>) getGenericClassType(0));
//                return mappingToDomain(data);
//            } else {
//                if (response.body().getErrorMessages() != null
//                        && !response.body().getErrorMessages().isEmpty()) {
//                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
//                } else {
//                    throw new ErrorMessageException(MainApplication.getAppContext().getString
//                            (R.string.default_request_error_unknown));
//                }
//            }
//        } else {
//            String messageError = ErrorHandler.getErrorMessage(response);
//            if (!TextUtils.isEmpty(messageError)) {
//                throw new ErrorMessageException(messageError);
//            } else {
//                throw new RuntimeException(String.valueOf(response.code()));
//            }
//        }
//    }
//
//    abstract O mappingToDomain(I data);
//}
