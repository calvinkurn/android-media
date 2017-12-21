package com.tkpd.library.utils;

import android.content.Context;
import android.net.Uri;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class URLParser {

	private Uri uri;

	public URLParser (String url) {
		CommonUtils.dumper(url);
		uri = Uri.parse(url);
	}

	public String getDepIDfromURI (Context context) {
		List<String> Path = uri.getPathSegments();
		String iden = null;
		for (int i = 1; i < Path.size(); i++) {
			if (i == 1) {
				iden = Path.get(i);
			} else {
				iden = iden+"_"+Path.get(i);
			}
		}
		return iden;
	}

	public String getQuery () {
		return uri.getQuery();
	}

	public ArrayList<String> getSetQueryKey () {
		Set<String> query = uri.getQueryParameterNames();
		ArrayList<String> returnquery = new ArrayList<String>();
		returnquery.addAll(query);
		return returnquery;
	}

	public ArrayList<String> getSetQueryValue () {
		ArrayList<String> keylist = getSetQueryKey();
		ArrayList<String> valuelist = new ArrayList<String>();
		for (int i = 0; i < keylist.size(); i++) {
			valuelist.add(uri.getQueryParameter(keylist.get(i)));
		}
		return valuelist;
	}

	public JSONObject getJSONObj () {
		ArrayList<String> keylist = getSetQueryKey();
		ArrayList<String> valuelist = getSetQueryValue();
		JSONObject json = new JSONObject();
		for (int i = 0; i < keylist.size(); i++) {
			try {
				json.putOpt(keylist.get(i), valuelist.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getType () {
		List<String> Path = uri.getPathSegments();
		return Path.get(0);
	}

	public String getHotAlias() {
		List<String> Path = uri.getPathSegments();
		return Path.get(1);
	}

	public static String getPathSegment(int i, String url) {
		List<String> path = Uri.parse(url).getPathSegments();
		if (path.size()>i)
			return path.get(i);
		else
			return "";
	}

	public HashMap<String, String> getParamKeyValueMap() {
		HashMap<String, String> map = new HashMap<>();
		ArrayList<String> keylist = getSetQueryKey();
		for (int i = 0; i < keylist.size(); i++) {
			map.put(keylist.get(i), uri.getQueryParameter(keylist.get(i)));
		}
		return map;
	}
}
