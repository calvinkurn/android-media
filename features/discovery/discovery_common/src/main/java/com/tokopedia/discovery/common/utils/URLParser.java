package com.tokopedia.discovery.common.utils;

import android.net.Uri;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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

	public HashMap<String, String> getParamKeyValueMapDecoded() {
		HashMap<String, String> map = new HashMap<>();
		ArrayList<String> keylist = getSetQueryKey();
		for (int i = 0; i < keylist.size(); i++) {
			map.put(keylist.get(i), decodeUtf8(uri.getQueryParameter(keylist.get(i))));
		}
		return map;
	}

	private String decodeUtf8(String encodedString) {
		try {
			return URLDecoder.decode(encodedString, "UTF-8");
		}
		catch (Exception e) {
			return encodedString;
		}
	}
}
