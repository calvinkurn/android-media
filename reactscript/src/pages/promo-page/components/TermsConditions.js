import React, { Component } from 'react'
import { View, Text, WebView, StyleSheet } from 'react-native'
import { AllHtmlEntities as Entities } from 'html-entities'


const entities = new Entities()

class TermsConds extends Component {
    render(){
        const { extra } = this.props.data
        const decodeHtml = entities.decode(extra)

        return (
            <WebView source={{html: decodeHtml}} />
        )
    }
 
    static navigationOptions = {
        header: null
    }
}

export default TermsConds