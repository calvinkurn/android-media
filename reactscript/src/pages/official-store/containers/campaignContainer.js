import React, { Component } from 'react'
import { View, ActivityIndicator } from 'react-native'
import { connect } from 'react-redux'
import { fetchCampaigns, addToWishlist } from '../actions/actions'
import CampaignList from '../components/campaignList'



class CampaignContainer extends Component {
  componentWillMount() {
    const { dispatch } = this.props
    const User_ID = this.props.screenProps.User_ID
    dispatch(fetchCampaigns(User_ID))
  }

  renderCampaign = (campaigns) => {
    return (
      this.props.campaigns.isFetching ? 
        <View style={{ marginTop:20, justifyContent:'center', alignItems:'center', flex:1}}>
          <ActivityIndicator size="large" />
        </View> :
        <CampaignList
          User_ID={this.props.screenProps.User_ID}
          campaigns={campaigns} />
    )
  }

  render() {
    const campaigns = this.props.campaigns.items
    // console.log(campaigns.length)
    // console.log(campaigns)
    // console.log(campaigns[0])
    if (campaigns.length == 1){
      return this.renderCampaign(campaigns[0])
    } else {
      return this.renderCampaign(campaigns)
    }
  }
}

const mapStateToProps = state => {
  const campaigns = state.campaigns
  return {
    campaigns
  }
}

export default connect(mapStateToProps)(CampaignContainer)