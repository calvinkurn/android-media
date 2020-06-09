package com.tokopedia.statistic.presentation.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.statistic.R
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : Fragment() {

    companion object {
        fun newInstance(): StatisticFragment {
            return StatisticFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_stc_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_stc_action_calendar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
            R.id.actionStcSelectDate -> selectDateRange()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(headerStcStatistic)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = activity.getString(R.string.stc_shop_statistic)
        }
    }

    private fun selectDateRange() {

    }


}