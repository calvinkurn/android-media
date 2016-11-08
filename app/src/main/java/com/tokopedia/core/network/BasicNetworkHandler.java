package com.tokopedia.core.network;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tokopedia.core.util.RequestManager;

/**
 * Class ini untuk melakukan fungsi networking dasar tanpa enkripsi
 *
 * @author ricoharisin
 *
 * DIfficulty level: Hard
 * development time: 5 days
 *
 */
@Deprecated
public class BasicNetworkHandler {
	/** object context */
	private Context context;
	/** URL untuk ke API */
	private String url;
	/** hash map untuk menyimpan parameter */
	private Map<String, String> params = new HashMap<String, String>();
	/** object listener NetworkResponse */
	private BasicNetworkResponse listenerResponse;
	/** class untuk melakukan http request */
	private StringRequest request;
	
	/**
	 * interface untuk handle saat melakukan http request
	 * @author ricoharisin
	 *
	 */
	public interface BasicNetworkResponse {
		/** 
		 * dipanggil ketika melakukan http request
		 * @param status berhasil terkoneksi atau tidak
		 */
		public void onSuccess(Boolean status);
		/**
		 * dipanggil ketika mendapatkan status code 200 dan response dari API
		 * @param Result hasil response dari API
		 */
		public void onResponse(String Result);
		/**
		 * dipanggil ketika terjadi error pada response dari API
		 * @param Msg pesan error
		 */
		public void onError(String Msg);
	}
	
	/**
	 * constructor untuk class network handler
	 * @param context
	 * @param url alamat API
	 */
	public BasicNetworkHandler (Context context, String url) {
		this.context = context;
		this.url = url;
	}
	
	/**
	 * constructor untuk class network handler
	 * @param context
	 */
	public BasicNetworkHandler (Context context) {
		this.context = context;
		this.url = url;
	}
	
	/**
	 * menambahkan parameter ke request
	 * @param key
	 * @param value
	 */
	public void addParam (String key, String value) {
		params.put(key, value);
	}
	
	/**
	 * menambahkan parameter ke request
	 * @param key
	 * @param value
	 */
	public void addParam (String key, int value) {
		params.put(key, Integer.toString(value));
	}
	
	/**
	 * melakukan proses http request menggunakan method POST
	 * ketika terjadi timeout maka aplikasi akan melakukan proses retry otomatis
	 * @param listener object listener NetworkResponse
	 */
	public void commit (BasicNetworkResponse listener) {
		this.listenerResponse = listener;
		request = new StringRequest(Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println(response);
				listenerResponse.onResponse(response);
				listenerResponse.onSuccess(true);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
				if (isNoConnection(error)) {

				} else if (isTimeout(error)) {
					System.out.println("retry again");
					startConnection();
				}
				
			}
		}) {
			@Override
            protected Map<String, String> getParams() {
                //params.put("value", KeyGenerator.getKey());
				
                return params;
            }
		};
		System.out.println(params.toString());
		startConnection();
	}
	
	/**
	 * menambahkan object request ke queue untuk dapat di proses
	 */
	private void startConnection() {
        RequestManager.getRequestQueue().add(request);
	}
	
	/**
	 * fungsi untuk mengecek apakah error disebabkan tidak ada koneksi apa bukan
	 * @param error object VolleyError yang di return dari class StringRequest
	 * @return boolean status
	 */
	private Boolean isNoConnection(VolleyError error) {
		return error.toString().contains("com.android.volley.NoConnectionError");
	}
	
	/**
	 * fungsi untuk mengecek apakah error disebabkan oleh server error atau bukan (500)
	 * @param error object VolleyError yang di return dari class StringRequest
	 * @return boolean status
	 */
	private Boolean isServerError(VolleyError error) {
		return error.toString().equals("com.android.volley.ServerError");
	}
	
	/**
	 * fungsi untuk mengecek apakah error disebabkan timeout apa bukan
	 * @param error object VolleyError yang di return dari class StringRequest
	 * @return boolean status
	 */
	private Boolean isTimeout(VolleyError error) {
		return !isNoConnection(error) && !isServerError(error);
	}

	public void commitPost (BasicNetworkResponse listener) {
		this.listenerResponse = listener;
		request = new StringRequest(Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println(response);
				listenerResponse.onResponse(response);
				listenerResponse.onSuccess(true);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
				if (isNoConnection(error)) {

				} else if (isTimeout(error)) {
					System.out.println("retry again");
					startConnection();
				}

			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				//params.put("value", KeyGenerator.getKey());

				return params;
			}
		};
		System.out.println(params.toString());
		startConnection();
	}
}
