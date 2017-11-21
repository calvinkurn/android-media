package com.tokopedia.abstraction.base.data.source.database;


import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nathan on 10/23/17.
 */

public interface DataListDBSource<T, U> extends DataDBSource<List<T>, List<U>> {

}
