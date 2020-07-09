package com.tokopedia.discovery.common.utils;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class URLParser {

	private Uri uri;

	public URLParser(String url) {
		uri = Uri.parse(url);
	}

	public ArrayList<String> getSetQueryKey () {
		Set<String> query = uri.getQueryParameterNames();
		ArrayList<String> returnquery = new ArrayList<String>();
		returnquery.addAll(query);
		return returnquery;
	}

	public HashMap<String, String> getParamKeyValueMap() {
		HashMap<String, String> map = new HashMap<>();
		ArrayList<String> keylist = getSetQueryKey();
		for (int i = 0; i < keylist.size(); i++) {
			map.put(keylist.get(i), uri.getQueryParameter(keylist.get(i)));
		}
		return map;
	}

	public static String getPathSegment(int i, String url) {
		List<String> path = Uri.parse(url).getPathSegments();
		if (path.size()>i)
			return path.get(i);
		else
			return "";
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
}
