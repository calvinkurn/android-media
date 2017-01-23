//package com.tokopedia.core.facade;
//
//import android.content.Context;
//
//import com.tokopedia.core.fragment.FragmentBuyCredit.Model;
//import com.tokopedia.core.network.NetworkHandler;
//import com.tokopedia.core.util.StringVariable;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Tkpd_Eka on 3/16/2015.
// */
//public class FacadeBuyCredit extends BaseFacade{
//
//    private static String GET_PULSA_DATA = "get_pulsa_data";
//
//    public interface GetProviderListener{
//        void onSuccess(List<Model> modelList);
//        void onFailed();
//        void onError(String message);
//    }
//
//    private static String URL = StringVariable.BELI_PULSA;
//
//    public static FacadeBuyCredit createInstance(Context context){
//        FacadeBuyCredit facade = new FacadeBuyCredit(context);
//        return facade;
//    }
//
//    public FacadeBuyCredit(Context context) {
//        super(context);
//    }
//
//    public void getProvider(GetProviderListener listener){
//        NetworkHandler network = new NetworkHandler(context, URL);
//        network.AddParam("act", GET_PULSA_DATA);
//        network.Commit(onGetProvider(listener));
//    }
//
//    private NetworkHandler.NetworkHandlerListener onGetProvider(final GetProviderListener listener){
//        return new NetworkHandler.NetworkHandlerListener() {
//            @Override
//            public void onSuccess(Boolean status) {
//                if(!status)
//                    listener.onFailed();
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//                listener.onSuccess(getProviderResult(Result));
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                listener.onError(MessageError.get(0));
//            }
//        };
//    }
//
//    private List<Model> getProviderResult(JSONObject Result){
//        List<Model> list = new ArrayList<>();
//        try {
//            JSONObject dataList = Result.getJSONObject("data_pulsa");
//            JSONArray pulsaDetailList = dataList.getJSONArray("pulsa_detail");
//            JSONArray providerDetailList = dataList.getJSONArray("provider_detail");
//            JSONObject[] pulsaDetailArray = new JSONObject[pulsaDetailList.length()];
//
//            int total = pulsaDetailList.length();
//
//            for(int i = 0 ; i < total ; i++){
//                pulsaDetailArray[i] = pulsaDetailList.getJSONObject(i);
//            }
//
//            total = providerDetailList.length();
//
//            for(int i = 0; i < total ; i++){
//                Model model = new Model();
//                JSONObject provider = providerDetailList.getJSONObject(i);
//                model.name = provider.getString("provider_name");
//                model.id = provider.getString("provider_id");
//                for(JSONObject detail : pulsaDetailArray){
//                    if(detail.getString("provider_id").equals(model.id)){
//                        model.priceList.add(detail.getString("pulsa_price"));
//                        model.valueList.add(detail.getString("pulsa_name"));
//                        model.valueId.add(detail.getString("pulsa_id"));
//                    }
//                }
//                list.add(model);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//}
