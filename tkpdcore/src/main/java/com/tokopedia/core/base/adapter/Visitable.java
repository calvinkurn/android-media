package com.tokopedia.core.base.adapter;

/**
 * @author kulomady on 1/24/17.
 */

/**
 * Use BaseAdapter (visitable pattern) from tkpd abstraction
 */
@Deprecated
public interface Visitable<T> {

    int type(T typeFactory);

}
