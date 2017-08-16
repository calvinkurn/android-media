import React, { Component } from 'react'
import { View, ActivityIndicator, DeviceEventEmitter } from 'react-native'
import { connect } from 'react-redux'
import { fetchCampaigns, addToWishlist, addToWishlistPDP, addWishlistFromPdp } from '../actions/actions'
import CampaignList from '../components/campaignList'



class CampaignContainer extends Component {
    componentWillMount() {
        const { dispatch } = this.props
        const User_ID = this.props.screenProps.User_ID
        dispatch(fetchCampaigns(User_ID))
        
        this.addToWishlist = DeviceEventEmitter.addListener("WishlistAdd", (res) => {
            dispatch(addWishlistFromPdp(res))
        });
        this.removeWishlist = DeviceEventEmitter.addListener("WishlistRemove", (res) => {
            dispatch(fetchCampaigns(User_ID))
        });
    }

    componentWillUnmount(){
        this.addToWishlist.remove()
        this.removeWishlist.remove()
    }
    
    renderCampaign = (campaigns) => {
        return (
            this.props.campaigns.isFetching ? 
            <View style={{ marginTop:20, marginBottom:20, justifyContent:'center', alignItems:'center', flex:1}}>
                <ActivityIndicator size="large" />
            </View> : 
            <CampaignList 
                User_ID={this.props.screenProps.User_ID} 
                campaigns={campaigns} />
        )
    }
    
    render() {
        const campaigns = this.props.campaigns.items
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