package com.tokopedia.tkpd.home;

import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.entity.topPicks.Group;
import com.tokopedia.core.network.entity.topPicks.Toppick;

import java.util.ArrayList;

/**
 * Created by Alifa on 12/29/2016.
 */

public interface TopPicksView {

    void renderTopPicks(ArrayList<Toppick> topicks);

}
