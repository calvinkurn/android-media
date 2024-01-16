package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.play.widget.databinding.FragmentPlayVideoWidgetSampleBinding
import com.tokopedia.play.widget.databinding.ItemPlayVideoWidgetSampleBinding
import com.tokopedia.play.widget.player.PlayVideoWidgetVideoManager
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import kotlin.time.Duration.Companion.seconds

/**
 * Created by kenny.hadisaputra on 19/10/23
 */
class PlayVideoWidgetSampleFragment : Fragment() {

    private var _binding: FragmentPlayVideoWidgetSampleBinding? = null
    private val binding get() = _binding!!

    private val videoWidgetManager by viewLifecycleBound(
        creator = {
            PlayVideoWidgetVideoManager(
                binding.root,
                viewLifecycleOwner,
                config = PlayVideoWidgetVideoManager.Config(autoPlayAmount = 2)
            )
        }
    )

    private val adapter by viewLifecycleBound(
        creator = { Adapter(videoWidgetManager) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayVideoWidgetSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
        adapter.submitList(
            List(5) {
                PlayVideoWidgetUiModel(
                    id = it.toString(),
                    totalView = "${it + 1}k",
                    title = "Title $it sangat panjang sekali, wow gila ini panjang banget lagi woi ini biar panjang",
                    avatarUrl = "",
                    partnerName = "Partner $it",
                    coverUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSC6rQmJ_kshzM2fwpkthP-Tg_3VugPnz_vrw&usqp=CAU",
                    videoUrl = "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=0e6a19506f8271eebfd736a5e8aa0102",
                    badgeUrl = "",
                    isLive = it % 2 == 0,
                    duration = 3.seconds
                )
            }
        )
    }

    class Adapter(private val manager: PlayVideoWidgetVideoManager) : ListAdapter<PlayVideoWidgetUiModel, ViewHolder>(
        object : DiffUtil.ItemCallback<PlayVideoWidgetUiModel>() {
            override fun areItemsTheSame(
                oldItem: PlayVideoWidgetUiModel,
                newItem: PlayVideoWidgetUiModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PlayVideoWidgetUiModel,
                newItem: PlayVideoWidgetUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemPlayVideoWidgetSampleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                manager
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    class ViewHolder(
        private val binding: ItemPlayVideoWidgetSampleBinding,
        manager: PlayVideoWidgetVideoManager
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setListener(object : PlayVideoWidgetView.Listener {
                override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
                    Log.d("VideoPlayer", "Video finished playing")
                }

                override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) {
//                    Toast.makeText(view.context, "Video error: ", Toast.LENGTH_SHORT).show()
                    Log.d("VideoPlayer", "Video error: $error")
                }
            })
            manager.bind(binding.root)
        }

        fun bind(model: PlayVideoWidgetUiModel) {
            binding.root.bind(model)
        }
    }
}
