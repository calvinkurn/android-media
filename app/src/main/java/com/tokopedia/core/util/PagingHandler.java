package com.tokopedia.core.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingData;
import com.tokopedia.core.selling.model.orderShipping.Paging;

/**
 * @since 27/11/2015
 * move code to RxJava and Retrofit 2 style.
 */
public class PagingHandler extends com.tokopedia.core.database.model.PagingHandler {


	public void setNewParameter(OrderShippingData Result){
		Paging pagingString = Result.getPaging();
		if(pagingString!=null){
			Log.d("MNORMANSYAH", " check Paging : "+pagingString.toString());
			if(pagingString.getUriNext()!=null&&!pagingString.getUriNext().equals("0")){
				hasNext = true;
			}else{
				hasNext = false;
			}

		}
		else {
			hasNext = false;
		}
	}

	public void setNewParameter(PagingHandlerModel pagingHanderModel){
		Gson gson = new GsonBuilder().create();
		if(pagingHanderModel!=null){
			Log.d("MNORMANSYAH", " check Paging : "+pagingHanderModel.toString());
			if(CommonUtils.checkStringNotNull(pagingHanderModel.uriNext)){
				hasNext = true;
			}else{
				hasNext = false;
			}

		}
		else {
			hasNext = false;
		}
	}
}
