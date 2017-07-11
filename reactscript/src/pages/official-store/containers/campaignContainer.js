import React, { Component } from 'react'
import { connect } from 'react-redux'
import { fetchCampaigns, addToWishlist } from '../actions/actions'
import CampaignList from '../components/campaignList'

class CampaignContainer extends Component {
  componentDidMount() {
    const { dispatch } = this.props
    dispatch(fetchCampaigns())
  }

  render() {
    const campaigns = this.props.campaigns.items
    return (
      this.props.campaigns.isFetching ? null :
        <CampaignList
          campaigns={campaigns} />
    )
  }
}

const mapStateToProps = state => {
  const campaigns = state.campaigns
  return {
    campaigns
  }
}

export default connect(mapStateToProps)(CampaignContainer)